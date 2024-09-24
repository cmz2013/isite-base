package org.isite.commons.web.sign;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.isite.commons.cloud.utils.SpelExpressionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.utils.RequestUtils.getRequest;

/**
 * @Description 使用AOP切面方式实现接口签名校验，与业务代码解耦，实现代码复用。
 * 通过@ConditionalOnProperty来控制自动配置是否生效
 * value 数组类型，配置属性名称
 * name 数组类型，与prefix组合使用，组成完整的配置属性名称。name与value不可以同时存在，也不可以同时不存在。
 * prefix 配置属性名称的前缀
 * matchIfMissing 缺少该配置属性时是否可以加载。如果为true，没有该配置属性时也会正常加载；反之则不会生效(默认false)
 * havingValue 比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Aspect
@Order(100)
@Component
@ConditionalOnProperty(name = "security.signature.enabled", havingValue = "true")
public class SignAspect {

    @Pointcut("@annotation(org.isite.commons.web.sign.Signed)")
    public void access() {
        //用于匹配持有@Signature 注解的方法
    }

    @Around("@annotation(signed)")
    public Object doBefore(ProceedingJoinPoint point, Signed signed) throws Throwable {
        HttpServletRequest request = getRequest();
        SpelExpressionUtils spelExpressionUtils = new SpelExpressionUtils(
                ((MethodSignature) point).getParameterNames(), point.getArgs());
        String signature = isNotBlank(signed.signature()) ?
                (String) spelExpressionUtils.getValue(signed.signature()) : request.getHeader(signed.signatureHeader());
        SignSecret secret = getBean(signed.secret());
        String appCode = isNotBlank(signed.appCode()) ?
                signed.appCode() : request.getHeader(signed.appCodeHeader());
        String password = secret.password(appCode);
        Verification verification = getBean(signed.verification());
        if (isNotBlank(password)) {
            isTrue(verification.verify(point, signed, signature, password), "invalid signature");
        } else {
            isTrue(verification.verify(signature, secret.apiKey(appCode)), "invalid signature");
        }
        return point.proceed();
    }
}