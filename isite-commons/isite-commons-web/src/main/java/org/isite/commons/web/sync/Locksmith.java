package org.isite.commons.web.sync;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.cloud.utils.SpelExpressionUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
    @Setter
    private BusyTimer busyTimer;
    /**
     * KEY的前缀
     */
    private String prefix = Constants.BLANK_STR;
    /**
     * 锁的过期时间（毫秒）
     */
    private long expire;

    @Setter
    private SpelExpressionUtils spelExpressionUtils;

    public Locksmith(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.locks = new ArrayList<>();
    }

    public void setPrefix(String prefix) {
        this.prefix = null == prefix ? Constants.BLANK_STR : prefix;
    }

    /**
     * @Description 构造锁芯。读取参数值，替换lock#name中的占位符
     * @param lock 锁注解
     */
    public LockCylinder getLockCylinder(Lock lock) {
        if (ArrayUtils.isEmpty(lock.keys())) {
            Assert.notBlank(lock.name(), "name and keys cannot be empty at the same time");
            return new LockCylinder(lock.name(), lock.reentry());
        }
        Object[] values = new Object[lock.keys().length];
        for (int i = Constants.ZERO; i < lock.keys().length; i++) {
            values[i] = spelExpressionUtils.getValue(lock.keys()[i]);
        }
        String keys = StringUtils.join(lock.delimiter(), values);
        return new LockCylinder(null == lock.name() || Constants.BLANK_STR.equals(lock.name()) ?
                keys : StringUtils.join(lock.delimiter(), lock.name(), keys), lock.reentry());
    }

    /**
     * 加锁
     */
    public boolean tryLock(List<LockCylinder> lockCylinders, long time) {
        Assert.isFalse(isLocked(), "already locked");
        Assert.notEmpty(lockCylinders, "lockCylinders cannot be empty");
        Assert.isTrue(time >= Constants.MINUTE_SECOND, "time must be greater than or equal to 1 minute");

        this.expire = System.currentTimeMillis() + time * Constants.THOUSAND;
        if (tryAcquire(lockCylinders)) {
            return Boolean.TRUE;
        }
        if (null == this.busyTimer) {
            return Boolean.FALSE;
        }
        while (this.busyTimer.run()) {
            if (tryAcquire(lockCylinders)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 获取全部锁
     */
    private boolean tryAcquire(List<LockCylinder> lockCylinders) {
        for (LockCylinder lockCylinder : lockCylinders) {
            String lock = tryAcquire(lockCylinder, Constants.ONE);
            if (null == lock) {
                //获取全部锁失败，但是已获取的锁，必须解锁
                this.unlock();
                return Boolean.FALSE;
            }
            this.locks.add(lock);
        }
        //如果当前时间大于锁设置的超时时间，获取锁已超时
        if (this.expire <= System.currentTimeMillis()) {
            log.error("tryLock timeout");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 获取锁。setIfAbsent(setnx): 如果为空就set值，并返回true；如果不为空不进行操作，并返回false
     */
    private String tryAcquire(LockCylinder lockCylinder, int count) {
        try {
            if (count > lockCylinder.getReentry()) {
                return null;
            }
            String lock = this.prefix + String.format("%s:%s", lockCylinder.getName(), count);
            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(
                    lock, String.valueOf(this.expire), Duration.ofMillis(this.expire - System.currentTimeMillis())))) {
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
        if (Long.parseLong(oldExpire) > System.currentTimeMillis()) {
            return tryAcquire(lockCylinder, count + Constants.ONE);
        }
        //锁已超时，重新获取锁（在 expire() 命令执行成功前，防止发生宕机的现象出现死锁的问题）
        //currentExpire 与 oldExpire 如果相等，说明当前 getset 设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了
        String currentExpire = redisTemplate.opsForValue().getAndSet(existLock, String.valueOf(this.expire));
        if (oldExpire.equals(currentExpire)) {
            redisTemplate.expire(existLock, Duration.ofMillis(this.expire - System.currentTimeMillis()));
            return existLock;
        }
        return tryAcquire(lockCylinder, count + Constants.ONE);
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
        if (CollectionUtils.isEmpty(locks)) {
            return Boolean.FALSE;
        }
        if (System.currentTimeMillis() >= this.expire) {
            this.locks.clear();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}