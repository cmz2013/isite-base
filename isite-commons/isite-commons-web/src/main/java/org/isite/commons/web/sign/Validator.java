package org.isite.commons.web.sign;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.commons.web.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Description 接口签名验证。可以通过 注解@Signed 的属性validator指定子类重写校验逻辑
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class Validator {

    /**
     * 接口签名有效期，单位：秒，如果没有配置则使用默认值30
     */
    @Value("${security.signature.validity:10}")
    private Long validity;

    public boolean verify(ProceedingJoinPoint point, Signed signed, String signature, String password)
            throws NoSuchAlgorithmException {
        HttpServletRequest request = RequestUtils.getRequest();
        String api = request.getRequestURI().contains(Constants.QUESTION_MARK) ?
                request.getRequestURI().substring(Constants.ZERO, request.getRequestURI().indexOf(Constants.QUESTION_MARK)) :
                request.getRequestURI();
        if (StringUtils.isNotBlank(request.getHeader(HttpHeaders.X_FORWARDED_PREFIX))) {
            api = request.getHeader(HttpHeaders.X_FORWARDED_PREFIX) + api;
        }
        return SignUtils.verifySignature(api, signature, password, Long.parseLong(request.getHeader(signed.timestampField())),
                validity, getSignatureParameter(point, signed));
    }

    /**
     * 获取签名参数
     */
    private Map<String, Object> getSignatureParameter(ProceedingJoinPoint point, Signed validated) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        //每个参数上可能有多个注解,是一个一维数组,多个参数又是一维数组,就组成了二维数组
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
        Object[] args = point.getArgs();
        Map<String, Object> signatureParameter = new HashMap<>();
        List<String> signatureFields = Arrays.asList(validated.fields());

        for (int index = Constants.ZERO; index < parameterNames.length; index++) {
            if (TypeUtils.isBasic(args[index])) {
                String signatureField = getSignatureField(parameterNames[index], annotations[index]);
                if (signatureFields.isEmpty() || signatureFields.contains(signatureField)) {
                    signatureParameter.put(signatureField, args[index]);
                }
            } else {
                //对象属性签名
                signatureParameter.putAll(SignUtils.getSignatureParameters(signatureFields, args[index]));
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

    public boolean verify(String signature, String apiKey) {
        return StringUtils.isNotBlank(signature) && signature.equals(apiKey);
    }
}
