package org.isite.commons.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.constants.HttpHeaders.CONTENT_TYPE;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_APP_CODE;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_SIGNATURE;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_TIMESTAMP;
import static org.isite.commons.web.sign.SignUtils.getSignature;
import static org.isite.commons.web.sign.SignUtils.getSignatureParameter;
import static org.isite.commons.cloud.utils.PropertyUtils.getApplicationName;
import static org.isite.commons.lang.Constants.AMPERSAND;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.EQUALS_SIGN;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.QUESTION_MARK;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.ChronoUnit.SECOND;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        if (isNotEmpty(signPassword)) {
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
        String apiName = url.contains(QUESTION_MARK) ? url.substring(ZERO, url.indexOf(QUESTION_MARK)) : url;
        template.header(X_APP_CODE, getApplicationName());
        long timestamp = currentTimeMillis() / SECOND.getMillis();
        template.header(X_TIMESTAMP, valueOf(timestamp));
        template.header(X_SIGNATURE, getSignature(apiName,
                getSignatureParameter(getRequestData(template)), password, timestamp));
    }

    /**
     * 获取签名数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestData(RequestTemplate template) {
        byte[] bytes = template.body();
        String body = isEmpty(bytes) ? BLANK_STRING : new String(bytes, template.requestCharset());
        if (isJson(template.headers().get(CONTENT_TYPE))) {
            //数组不参与签名
            return body.startsWith(JSON_OBJECT_PREFIX) ?
                    parseObject(body, HashMap.class, String.class, Object.class) : emptyMap();
        }

        Map<String, Object> data = new HashMap<>();
        //默认按application/x-www-form-urlencoded格式处理
        parseQueryData(body, data);
        String url = template.url();
        String params = url.substring(url.indexOf(QUESTION_MARK) + ONE);
        if (isNotBlank(params)) {
            parseQueryData(params, data);
        }
        return data;
    }

    /**
     * 请求参数是否为JSON格式
     */
    private boolean isJson(Collection<String> contentTypes) {
        if (isNotEmpty(contentTypes)) {
            for (String contentType : contentTypes) {
                if (contentType.contains(APPLICATION_JSON_VALUE)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析url或body中的query参数（key=value）
     */
    private void parseQueryData(String params, Map<String, Object> data) {
        for (String pairs : params.split(AMPERSAND)) {
            if (isBlank(pairs)) {
                continue;
            }
            int index = pairs.indexOf(EQUALS_SIGN);
            if (index > ZERO) {
                data.put(pairs.substring(ZERO, index), pairs.substring(index + ONE));
            }
        }
    }
}