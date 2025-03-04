package org.isite.commons.web.mq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Message {

    String queues();

    Class<? extends Producer> producer();
}
