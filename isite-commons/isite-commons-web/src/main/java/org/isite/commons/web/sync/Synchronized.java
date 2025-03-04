package org.isite.commons.web.sync;

import org.isite.commons.lang.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Description 申明方法执行前使用分布式并发锁控制。
 * 项目必须引用和配置spring-boot-starter-data-redis，分布式并发锁的切面配置才会生效。
 * 1. @Target 标记这个注解应该是哪种Java成员：
 * 1）ElementType.TYPE：类，接口（包括注解类型）和枚举
 * 2）ElementType.METHOD：方法
 * 3）ElementType.CONSTRUCTOR： 构造函数
 * 4）ElementType.PARAMETER： 参数
 * 5）ElementType.LOCAL_VARIABLE： 本地变量
 *
 * 2. @Retention 标识这个注解怎么保存：
 * 1）RetentionPolicy.RUNTIME：在运行期间
 * 2）RetentionPolicy.CLASS：在类文件中，运行期间会丢弃
 * 3）RetentionPolicy.SOURCE：在源码级别，编译时会丢弃
 *
 * 3. @Inherited 标识这个注解允许被继承
 * 4. @Documented 生成Java文档
 * @Author <font color='blue'>zhangcm</font>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Synchronized {
    /**
     * 加锁，locks.length > 1时，批量加锁
     */
    Lock[] locks();
    /**
     * KEY的前缀
     */
    String prefix() default Constants.BLANK_STR;
    /**
     * 设置锁的有效时间（秒），防止死锁
     */
    long time() default Constants.MINUTE_SECOND;
    /**
     * 忙时等待时间（毫秒）。默认不等待，即并发冲突时立即中断请求
     */
    long waiting() default Constants.ZERO;
    /**
     * 忙时重试次数，waiting > 0 时该配置生效
     */
    int retry() default Constants.ONE;
}
