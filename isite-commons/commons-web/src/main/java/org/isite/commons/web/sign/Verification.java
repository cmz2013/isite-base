package org.isite.commons.web.sign;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.isite.commons.cloud.sign.SignField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.sign.SignUtils.getSignatureParameters;
import static org.isite.commons.cloud.sign.SignUtils.verifySignature;
import static org.isite.commons.lang.data.Constants.QUESTION_MARK;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.http.HttpHeaders.X_FORWARDED_PREFIX;
import static org.isite.commons.lang.utils.TypeUtils.isBasic;
import static org.isite.commons.web.utils.RequestUtils.getRequest;

/**
 * @Description 接口签名验证
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class Verification {

    /**
     * 接口签名有效期，单位：秒，如果没有配置则使用默认值30
     */
    @Value("${security.signature.validity:10}")
    private Long validity;

    /**
     * @param signature 接口签名
     * @param target 正确的签名
     * @return 接口签名是否正确
     */
    public boolean verify(String signature, String target) {
        return isNotBlank(signature) && signature.equals(target);
    }

    public boolean verify(ProceedingJoinPoint point, Signed signed, String signature, String password)
            throws NoSuchAlgorithmException {
        HttpServletRequest request = getRequest();
        String api = request.getRequestURI().contains(QUESTION_MARK) ?
                request.getRequestURI().substring(ZERO, request.getRequestURI().indexOf(QUESTION_MARK)) :
                request.getRequestURI();
        if (isNotBlank(request.getHeader(X_FORWARDED_PREFIX))) {
            api = request.getHeader(X_FORWARDED_PREFIX) + api;
        }
        return verifySignature(api, signature, password, parseLong(request.getHeader(signed.timestampHeader())),
                validity, getSignatureParameter(point, signed));
    }

    /**
     * 获取签名参数
     */
    private Map<String, Object> getSignatureParameter(ProceedingJoinPoint point, Signed validated) {
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
