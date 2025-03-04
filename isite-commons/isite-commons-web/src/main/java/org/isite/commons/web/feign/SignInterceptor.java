package org.isite.commons.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.cloud.utils.PropertyUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.http.ContentType;
import org.isite.commons.web.sign.SignUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description 如果在请求头设置feign-sign-password，该拦截器自动设置签名信息。
 * RequestInterceptor Bean是用于全局配置，即所有 Feign 客户端都会应用该拦截器。
 * Feign的构建器添加的请求拦截器，作用范围是该Feign客户端的所有请求。
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SignInterceptor implements RequestInterceptor {

    private static final String JSON_OBJECT_PREFIX = "{";
    /**
     * 如果在请求头设置feign-sign-password，该拦截器自动设置签名信息。
     */
    public static final String FEIGN_SIGN_PASSWORD = "feign-sign-password";
    
    /**
     * @Description 添加签名信息
     */
    @Override
    public void apply(RequestTemplate template) {
        Collection<String> signPassword = template.headers().get(FEIGN_SIGN_PASSWORD);
        if (CollectionUtils.isNotEmpty(signPassword)) {
            setSignature(template, signPassword.iterator().next());
            // remove from Header
            template.removeHeader(FEIGN_SIGN_PASSWORD);
        }
    }

    /**
     * 在Http Header中设置签名信息
     */
    private void setSignature(RequestTemplate template, String password) {
        String url = template.url();
        String apiName = url.contains(Constants.QUESTION_MARK) ?
                url.substring(Constants.ZERO, url.indexOf(Constants.QUESTION_MARK)) : url;
        template.header(HttpHeaders.X_APP_CODE, PropertyUtils.getApplicationName());
        long timestamp = System.currentTimeMillis() / ChronoUnit.SECOND.getMillis();
        template.header(HttpHeaders.X_TIMESTAMP, String.valueOf(timestamp));
        template.header(HttpHeaders.X_SIGNATURE, SignUtils.getSignature(apiName,
                SignUtils.getSignatureParameter(getRequestData(template)), password, timestamp));
    }

    /**
     * 获取签名数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestData(RequestTemplate template) {
        byte[] bytes = template.body();
        String body = ArrayUtils.isEmpty(bytes) ? Constants.BLANK_STR : new String(bytes, template.requestCharset());
        if (isJson(template.headers().get(HttpHeaders.CONTENT_TYPE))) {
            //数组不参与签名
            return body.startsWith(JSON_OBJECT_PREFIX) ? Jackson.parseObject(
                    body, HashMap.class, String.class, Object.class) : Collections.emptyMap();
        }
        Map<String, Object> data = new HashMap<>();
        //默认按application/x-www-form-urlencoded格式处理
        parseQueryData(body, data);
        String url = template.url();
        String params = url.substring(url.indexOf(Constants.QUESTION_MARK) + Constants.ONE);
        if (StringUtils.isNotBlank(params)) {
            parseQueryData(params, data);
        }
        return data;
    }

    /**
     * 请求参数是否为JSON格式
     */
    private boolean isJson(Collection<String> contentTypes) {
        if (CollectionUtils.isNotEmpty(contentTypes)) {
            for (String contentType : contentTypes) {
                if (contentType.contains(ContentType.APPLICATION_JSON)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 解析url或body中的query参数（key=value）
     */
    private void parseQueryData(String params, Map<String, Object> data) {
        for (String pairs : params.split(Constants.AMPERSAND)) {
            if (StringUtils.isBlank(pairs)) {
                continue;
            }
            int index = pairs.indexOf(Constants.EQUAL_SIGN);
            if (index > Constants.ZERO) {
                data.put(pairs.substring(Constants.ZERO, index), pairs.substring(index + Constants.ONE));
            }
        }
    }
}