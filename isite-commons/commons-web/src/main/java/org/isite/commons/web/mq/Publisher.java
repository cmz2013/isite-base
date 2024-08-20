package org.isite.commons.web.mq;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description 方法执行成功以后，根据返回数据发送 MQ 消息
 * @Author <font color='blue'>zhangcm</font>
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Publisher {

    Message[] messages();
}
