package org.isite.commons.web.sync;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.cloud.spel.VariableExpression.getValue;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.data.Constants.MINUTE_SECONDS;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.lang.template.FreeMarker.process;
import static org.isite.commons.lang.utils.TypeUtils.castArray;
import static org.isite.commons.lang.utils.TypeUtils.isBasic;

/**
 * Redis锁匠(非线程安全，每个线程创建各自独立的实例)，主要功能：
 * 1) 使用FreeMarker模板引擎生成锁实例
 * 2）加锁
 * 3）释放锁
 *
 * @author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class Locksmith {

    private static final String EXPRESSION_PLACEHOLDER_ARG = "arg";
    /**
     * KEY默认的前缀
     */
    public static final String DEFAULT_PREFIX = "isite:synchronized:";
    /**
     * 默认的可重入次数
     */
    public static final int DEFAULT_REENTRY = ONE;
    /**
     * 设置锁的有效时长（秒），防止死锁。锁的有效时长要大于处理请求的时间，但是不能过大。
     */
    private long time = MINUTE_SECONDS;
    /**
     * KEY的前缀
     */
    private String prefix = DEFAULT_PREFIX;
    /**
     * redis客户端
     */
    private final RedisTemplate<String, Long> redisTemplate;
    /**
     * 锁实例
     */
    private final List<String> locks;
    /**
     * 锁的过期时间（毫秒）
     */
    private long expire;
    /**
     * 忙时等待定时器
     */
    private BusyTimer busyTimer;

    public Locksmith(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.locks = new ArrayList<>();
    }

    public Locksmith(RedisTemplate<String, Long> redisTemplate, BusyTimer busyTimer) {
        this(redisTemplate);
        this.busyTimer = busyTimer;
    }

    public void setTime(long time) {
        isTrue(time >= MINUTE_SECONDS, "must be greater than " + MINUTE_SECONDS);
        this.time = time;
    }

    public void setPrefix(String prefix) {
        this.prefix = null == prefix ? BLANK_STRING : prefix;
    }

    /**
     * 获取锁柱。读取对象属性值，替换key中的占位符（freemarker模板引擎）
     * @param lock 锁定义
     * @param parameterNames 参数名
     * @param args 参数
     */
    public List<LockCylinder> getLockCylinder(Lock lock, String[] parameterNames, Object[] args) {
        List<LockCylinder> lockCylinders = new ArrayList<>(lock.keys().length);
        for (int i = 0; i < lock.keys().length; i++) {
            Object value = getValue(lock.keys()[i], parameterNames, args);
            if (null == value) {
                continue;
            }
            if (value instanceof Collection<?>) {
                lockCylinders.addAll(getLockCylinder(lock, (Collection<?>) value, i));
            } else if (value.getClass().isArray()) {
                lockCylinders.addAll(getLockCylinder(lock, castArray(value, Object.class), i));
            } else {
                lockCylinders.add(getLockCylinder(lock, value, i));
            }
        }
        return lockCylinders;
    }

    /**
     * 使用锁数据替换key中的占位符（freemarker模板引擎）
     */
    @SneakyThrows 
    private List<LockCylinder> getLockCylinder(Lock lock, Object[] values, int index) {
        if (isEmpty(values)) {
            return emptyList();
        }
        List<LockCylinder> results = new ArrayList<>(values.length);
        for (Object value : values) {
            results.add(getLockCylinder(lock, value, index));
        }
        return results;
    }

    /**
     * 使用锁数据替换key中的占位符（freemarker模板引擎）
     */
    @SneakyThrows
    private List<LockCylinder> getLockCylinder(Lock lock, Collection<?> values, int index) {
        if (values.isEmpty()) {
            return emptyList();
        }
        List<LockCylinder> results = new ArrayList<>(values.size());
        for (Object value : values) {
            results.add(getLockCylinder(lock, value, index));
        }
        return results;
    }

    /**
     * 使用数据替换name中的占位符（freemarker模板引擎）
     */
    @SneakyThrows
    private LockCylinder getLockCylinder(Lock lock, Object value, int index) {
        if (isBasic(value)) {
            Map<String, Object> map = new HashMap<>();
            map.put(EXPRESSION_PLACEHOLDER_ARG + index, value);
            value = map;
        }
        return new LockCylinder(process(lock.name(), value), lock.reentry());
    }

    /**
     * 加锁
     */
    public boolean tryLock(List<LockCylinder> lockCylinders) {
        isFalse(isLocked(), "there are locks that have not yet been released");
        if (isEmpty(lockCylinders)) {
            return FALSE;
        }
        if (tryAcquire(lockCylinders)) {
            return TRUE;
        }
        if (null == this.busyTimer) {
            return FALSE;
        }
        while (this.busyTimer.run()) {
            if (tryAcquire(lockCylinders)) {
                return TRUE;
            }
        }
        return FALSE;
    }

    /**
     * 获取全部锁
     */
    private boolean tryAcquire(List<LockCylinder> lockCylinders) {
        this.expire = currentTimeMillis() + this.time * THOUSAND;
        for (LockCylinder lockCylinder : lockCylinders) {
            String lock = tryAcquire(lockCylinder, ONE);
            if (null == lock) {
                //获取全部锁失败，但是已获取的锁，必须解锁
                this.unlock();
                return false;
            }
            this.locks.add(lock);
        }
        //如果当前时间大于锁设置的超时时间，获取锁已超时
        if (this.expire <= currentTimeMillis()) {
            log.error("tryLock timeout");
            return false;
        }
        return true;
    }

    /**
     * 获取锁。setIfAbsent(setnx): 如果为空就set值，并返回true；如果不为空不进行操作，并返回false
     */
    private String tryAcquire(LockCylinder lockCylinder, int count) {
        try {
            if (count > lockCylinder.getReentry()) {
                return null;
            }
            String lock = this.prefix + format("%s:%s", lockCylinder.getName(), count);
            if (TRUE.equals(redisTemplate.opsForValue().setIfAbsent(
                    lock, this.expire, ofMillis(this.expire - currentTimeMillis())))) {
                return lock;
            }
            return tryAcquire(lockCylinder, count, lock);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @Description 加锁。setnx 执行成功后，在 expire() 命令执行成功前，需要防止发生宕机的现象出现死锁的问题
     */
    private String tryAcquire(LockCylinder lockCylinder, int count, String existLock) {
        Long oldExpire = redisTemplate.opsForValue().get(existLock);
        //如果oldExpire为空，说明这个锁已经被释放，重新获取该锁
        if (null == oldExpire) {
            return tryAcquire(lockCylinder, count);
        }
        if (oldExpire > currentTimeMillis()) {
            return tryAcquire(lockCylinder, count + ONE);
        }
        /*
         * 锁已超时，重新获取锁（在 expire() 命令执行成功前，防止发生宕机的现象出现死锁的问题）
         * currentExpire 与 oldExpire 如果相等，说明当前 getset 设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了
         */
        Object currentExpire = redisTemplate.opsForValue().getAndSet(existLock, this.expire);
        if (oldExpire.equals(currentExpire)) {
            redisTemplate.expire(existLock, ofMillis(this.expire - currentTimeMillis()));
            return existLock;
        }
        return tryAcquire(lockCylinder, count + ONE);
    }

    /**
     * 释放锁信息
     */
    public void unlock() {
        if (isLocked()) {
            for (String lock : this.locks) {
                redisTemplate.delete(lock);
            }
            this.locks.clear();
        }
    }

    /**
     * @Description 是否已加锁
     * 比较当前时间和锁设置的超时时间，如果当前时间大于等于锁设置的超时时间，锁自动释放
     */
    private boolean isLocked() {
        if (isEmpty(locks)) {
            return false;
        }
        if (currentTimeMillis() >= this.expire) {
            this.locks.clear();
            return false;
        }
        return true;
    }
}