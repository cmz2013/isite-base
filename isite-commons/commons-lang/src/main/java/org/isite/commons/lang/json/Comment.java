package org.isite.commons.lang.json;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description 用于描述字段或方法的注解
 * @Author <font color='blue'>zhangcm</font>
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Comment {

  String value();
}