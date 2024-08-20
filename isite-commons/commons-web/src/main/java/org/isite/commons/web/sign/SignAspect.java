package org.isite.commons.web.sign;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.isite.commons.cloud.sign.SignField;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.constants.CloudConstants.X_APP_CODE;
import static org.isite.commons.cloud.constants.CloudConstants.X_SIGNATURE;
import static org.isite.commons.cloud.constants.CloudConstants.X_TIMESTAMP;
import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.PropertyUtils.getProperty;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.QUESTION_MARK;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.http.HttpHeaders.X_FORWARDED_PREFIX;
import static org.isite.commons.cloud.sign.SignUtils.getSignatureParameters;
import static org.isite.commons.cloud.sign.SignUtils.validateSignature;
import static org.isite.commons.lang.utils.TypeUtils.isBasic;
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

    @Pointcut("@annotation(org.isite.commons.web.sign.Signature)")
    public void access() {
        //用于匹配持有@Signature 注解的方法
    }

    @Around("@annotation(signature)")
    public Object doBefore(ProceedingJoinPoint point, Signature signature) throws Throwable {
        Long validity = getProperty("security.signature.validity", Long.class);
        if (null == validity) {
            validity = 30L;
        }

        HttpServletRequest request = getRequest();
        long timestamp = parseLong(request.getHeader(X_TIMESTAMP));
        String api = request.getRequestURI().contains(QUESTION_MARK) ?
                request.getRequestURI().substring(ZERO, request.getRequestURI().indexOf(QUESTION_MARK)) :
                request.getRequestURI();

        String prefix = request.getHeader(X_FORWARDED_PREFIX);
        if (isNotBlank(prefix)) {
            api = prefix + api;
        }

        String password = getBean(signature.secret()).password(request.getHeader(X_APP_CODE));
        isTrue(validateSignature(api, request.getHeader(X_SIGNATURE), password, timestamp, validity,
                getSignatureParameter(point, signature)), api + " invalid signature");
        return point.proceed();
    }

    /**
     * 获取签名参数
     */
    private Map<String, Object> getSignatureParameter(ProceedingJoinPoint point, Signature validated) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = point.getArgs();
        //每个参数上可能有多个注解,是一个一维数组,多个参数又是一维数组,就组成了二维数组
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
        Map<String, Object> signatureParameter = new HashMap<>();
        List<String> signatureFields = asList(validated.fields());

        for (int index = ZERO; index < parameterNames.length; index++) {
            if (isBasic(args[index])) {
                String signatureField = getSignatureField(parameterNames[index], annotations[index]);
                if (signatureFields.isEmpty() || signatureFields.contains(signatureField)) {
                    signatureParameter.put(signatureField, args[index]);
                }
            } else {
                //对象属性签名
                signatureParameter.putAll(getSignatureParameters(signatureFields, args[index]));
            }
        }
        return signatureParameter;
    }

    /**
     * @Description 获取签名字段名称
     * @param parameterName 参数变量名
     * @param annotations 参数注解
     */
    private String getSignatureField(String parameterName, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof SignField) {
                return ((SignField) annotation).value();
            }
        }
        return parameterName;
    }
}