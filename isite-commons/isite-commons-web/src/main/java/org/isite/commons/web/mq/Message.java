package org.isite.commons.web.mq;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Message {

    String queues();

    Class<? extends Producer> producer();
}
