package org.isite.commons.web.sign;

import org.isite.commons.cloud.sign.SignSecret;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 标记方法在执行前，执行签名验证
 * @Author <font color='blue'>zhangcm</font>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Signature {
    /**
     * 指定签名字段，如果空，默认全部字段
     */
    String[] fields() default {};

    /**
     * 在校验签名时使用的签名秘钥，从Request Header中读取appCode（@see SignAspect）
     */
    Class<? extends SignSecret> secret() default SignSecret.class;
}
