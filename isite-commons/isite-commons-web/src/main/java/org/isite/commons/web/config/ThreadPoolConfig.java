package org.isite.commons.web.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
/**
 * @Description 配置线程池
 * 1）@EnableAsync，开启 Spring Bean的异步方法（@Async）
 * 2）多个AOP切面组件，order越小优先级越高，优先级高的先进入后退出：
 * @Async 执行顺序是 Ordered.LOWEST_PRECEDENCE（EnableAsync#order()）
 * @Synchronized 执行顺序是Ordered.HIGHEST_PRECEDENCE（SyncAspect#getOrder() ）
 * @Cached jetCache注解缓存执行顺序是 Ordered.HIGHEST_PRECEDENCE（EnableMethodCache#order()）
 *
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class ThreadPoolConfig implements AsyncConfigurer {

    /**
     * @Description ThreadPoolExecutor 是Java并发包（java.util.concurrent）中的一个核心类，用于管理和控制线程池的执行
     * execute 方法接受一个 Runnable 任务。
     * submit 方法既可以接受 Runnable 任务，也可以接受 Callable 任务。
     * 1）对于 Runnable 任务，submit 方法返回一个 Future<?> 对象，其 get 方法返回 null；
     * 2）对于 Callable 任务，submit 方法返回一个 Future<T> 对象，其 get 方法返回 Callable 的执行结果。
     */
    @Bean
    @Primary
    @Override
    public Executor getAsyncExecutor() {
        //ThreadPoolTaskExecutor是Spring框架中的一个线程池实现，它基于JDK的ThreadPoolExecutor，并提供了更方便的配置和使用
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //优雅停机,调度器shutdown被调用时等待当前被调度的任务完成
        taskExecutor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        //等待的时间，不能无限的等待下去
        taskExecutor.setAwaitTerminationSeconds(1800);
        taskExecutor.initialize();
        //创建线程池，使用 TtlExecutors 进行包装,在任务执行时正确传递线程变量
        return TtlExecutors.getTtlExecutorService(taskExecutor.getThreadPoolExecutor());
    }

    /**
     * 异步异常处理类
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}