package org.isite.commons.web.sign;

import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.lang.Constants;

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
public @interface Signed {
    /**
     * 从请求参数获取签名的SpEL表达式，主要用于第三方接口签名验证。如果signature为空则默认从请求头获取签名。
     */
    String signature() default Constants.BLANK_STR;
    /**
     * 获取appCode，主要用于第三方接口签名验证。如果appCode为空则默认从请求头获取。
     */
    String appCode() default Constants.BLANK_STR;
    /**
     * 指定用于生成签名的字段，如果空则默认为全部字段。
     */
    String[] fields() default {};

    /**
     * 获取签名或生成签名时使用的秘钥
     */
    Class<? extends SignSecret> secret() default SignSecret.class;

    /**
     * 从请求头读取数据，如果没有配置则使用默认值字段名
     */
    String appCodeField() default HttpHeaders.X_APP_CODE;
    String signatureField() default HttpHeaders.X_SIGNATURE;
    String timestampField() default HttpHeaders.X_TIMESTAMP;
    /**
     * 签名校验，可以定义子类重写校验逻辑
     */
    Class<? extends Validator> validator() default Validator.class;
}
