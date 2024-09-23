package org.isite.commons.web.sync;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.cloud.spel.VariableExpression.getValue;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.MINUTE_SECONDS;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.template.FreeMarker.process;

/**
 * @Description 锁匠：构造锁、加锁、释放锁
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class Locksmith {
    /**
     * redis客户端
     */
    private final StringRedisTemplate redisTemplate;
    /**
     * 锁实例
     */
    private final List<String> locks;
    /**
     * 忙时等待定时器
     */
    private final BusyTimer busyTimer;
    /**
     * KEY的前缀
     */
    private String prefix = BLANK_STRING;
    /**
     * 锁的过期时间（毫秒）
     */
    private long expire;

    public Locksmith(StringRedisTemplate redisTemplate, BusyTimer busyTimer) {
        this.redisTemplate = redisTemplate;
        this.busyTimer = busyTimer;
        this.locks = new ArrayList<>();
    }

    public void setPrefix(String prefix) {
        this.prefix = null == prefix ? BLANK_STRING : prefix;
    }

    /**
     * @Description 构造锁芯。读取参数值，替换lock#name中的占位符
     * @param lock 锁注解
     * @param parameterNames 参数名
     * @param args 参数
     */
    public LockCylinder getLockCylinder(Lock lock, String[] parameterNames, Object[] args)
            throws TemplateException, IOException {
        if (isEmpty(lock.keys())) {
            return new LockCylinder(lock.name(), lock.reentry());
        }
        Map<String, Object> valueMap = new HashMap<>();
        for (int i = ZERO; i < lock.keys().length; i++) {
            Object value = getValue(lock.keys()[i], parameterNames, args);
            notNull(value, "the value of the expression is null: " + lock.keys()[i]);
            isFalse(value instanceof Collection<?>, "the value of the expression is collection: " + lock.keys()[i]);
            isFalse(value.getClass().isArray(), "the value of the expression is array: " + lock.keys()[i]);
            valueMap.put(lock.keys()[i].substring(ONE), value);
        }
        return new LockCylinder(process(lock.name(), valueMap), lock.reentry());
    }

    /**
     * 加锁
     */
    public boolean tryLock(List<LockCylinder> lockCylinders, long time) {
        isFalse(isLocked(), "already locked");
        isTrue(time >= MINUTE_SECONDS, "time must be greater than or equal to 1 minute");
        if (isEmpty(lockCylinders)) {
            return FALSE;
        }

        this.expire = currentTimeMillis() + time * THOUSAND;
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
        for (LockCylinder lockCylinder : lockCylinders) {
            String lock = tryAcquire(lockCylinder, ONE);
            if (null == lock) {
                //获取全部锁失败，但是已获取的锁，必须解锁
                this.unlock();
                return FALSE;
            }
            this.locks.add(lock);
        }
        //如果当前时间大于锁设置的超时时间，获取锁已超时
        if (this.expire <= currentTimeMillis()) {
            log.error("tryLock timeout");
            return FALSE;
        }
        return TRUE;
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
                    lock, valueOf(this.expire), ofMillis(this.expire - currentTimeMillis())))) {
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
        String oldExpire = redisTemplate.opsForValue().get(existLock);
        //如果oldExpire为空，说明这个锁已经被释放，重新获取该锁
        if (null == oldExpire) {
            return tryAcquire(lockCylinder, count);
        }
        //锁已超时，重新获取锁（在 expire() 命令执行成功前，防止发生宕机的现象出现死锁的问题）
        if (parseLong(oldExpire) > currentTimeMillis()) {
            return tryAcquire(lockCylinder, count + ONE);
        }
        //currentExpire 与 oldExpire 如果相等，说明当前 getset 设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了
        String currentExpire = redisTemplate.opsForValue().getAndSet(existLock, valueOf(this.expire));
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
     * @Description 是否已加锁.如果当前时间大于等于锁设置的超时时间，锁自动释放
     */
    private boolean isLocked() {
        if (isEmpty(locks)) {
            return FALSE;
        }
        if (currentTimeMillis() >= this.expire) {
            this.locks.clear();
            return FALSE;
        }
        return TRUE;
    }
}