package org.isite.commons.web.mq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Description 方法执行成功以后，根据返回数据发送 MQ 消息
 * @Author <font color='blue'>zhangcm</font>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publisher {

    Message[] messages();
}
