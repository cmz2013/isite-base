package org.isite.commons.web.sync;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.cloud.spel.VariableExpression.getValue;
import static org.isite.commons.lang.utils.TypeUtils.cast;

/**
 * @Description 分布式并发锁切面编程。
 * 实现接口 Ordered#getOrder方法，用于设置多个 Aspect 执行的先后顺序，order越小优先级越高。也可以使用@Order注解代替接口
 * 多个Aspect，在进来时，优先级最高的首先运行；在出去时，优先级最高的最后运行。
 * Spring Aspect事务默认最小优先级。SyncAspect设置最高优先级，是为了确保在Spring Aspect事务的外边加锁。如果在事务内加锁，事务未提交（数据还未持久化到数据库）但锁已释放，会导致分布所失效
 *
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Aspect
@Component
@ConditionalOnClass(value = RedisAutoConfiguration.class)
public class SyncAspect implements Ordered {

    private RedisTemplate<String, Long> redisTemplate;

    /**
     * 在方法上面标注 @Pointcut 注解，用来定义切入点
     * 表达式标签 @annotation()：用于匹配当前执行方法持有指定注解
     */
    @Pointcut("@annotation(org.isite.commons.web.sync.Synchronized)")
    public void access() {
        //用于匹配持有Synchronized注解的方法
    }

    @SneakyThrows
    @Around("@annotation(sync)")
    public Object doBefore(ProceedingJoinPoint point, Synchronized sync) {
        Locksmith locksmith = new Locksmith(redisTemplate, new BusyTimer(sync.waiting(), sync.retry()));
        locksmith.setPrefix(sync.prefix());
        locksmith.setTime(sync.time());

        List<LockCylinder> lockCylinders = new ArrayList<>(sync.locks().length);
        MethodSignature signature = (MethodSignature) point.getSignature();
        stream(sync.locks()).forEach(lock -> {
            if (TRUE.equals(getValue(lock.condition(), signature.getParameterNames(), point.getArgs()))) {
                if (isEmpty(lock.keys())) {
                    lockCylinders.add(new LockCylinder(lock.name(), lock.reentry()));
                } else {
                    lockCylinders.addAll(locksmith.getLockCylinder(lock, signature.getParameterNames(), point.getArgs()));
                }
            }
        });

        if (!locksmith.tryLock(lockCylinders)) {
            throw new ConcurrentError();
        }
        try {
            return point.proceed();
        } finally {
            locksmith.unlock();
        }
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
        this.redisTemplate = cast(redisTemplate);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}