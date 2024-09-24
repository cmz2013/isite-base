package org.isite.commons.web.sign;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyMap;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.MapUtils.isEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_TIMESTAMP;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.Constants.AMPERSAND;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.EQUAL_SIGN;
import static org.isite.commons.lang.Constants.THREE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.Reflection.getValue;
import static org.isite.commons.lang.encoder.Md5Encoder.digest;
import static org.isite.commons.lang.enums.ChronoUnit.SECOND;
import static org.isite.commons.lang.utils.TypeUtils.isBasic;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class SignUtils {
    /**
     * 签名字段：密码
     */
    private static final String PASSWORD = "password";
    /**
     * 时间误差：允许其他系统比本地时间快3秒
     */
    private static final int ERROR_SECOND = THREE;

    private SignUtils() {
    }

    /**
     * 获取签名加密串
     */
    @SneakyThrows
    public static String getSignature(String api, Map<String, Object> data, String password, long timestamp) {
        return digest(getPlaintext(api, data, password, timestamp));
    }

    /**
     * 获取签名明文
     */
    private static String getPlaintext(String api, Map<String, Object> data, String password, long timestamp) {
        List<String> keys = new ArrayList<>();
        data.forEach((key, value) -> {
            if (isBasic(value)) {
                keys.add(key);
            }
        });

        keys.add(X_TIMESTAMP);
        Collections.sort(keys);
        StringBuilder context = new StringBuilder(api).append(AMPERSAND);
        keys.forEach(key -> {
            if (X_TIMESTAMP.equals(key)) {
                context.append(key).append(EQUAL_SIGN).append(timestamp).append(AMPERSAND);
            } else {
                context.append(key).append(EQUAL_SIGN)
                        .append(null == data.get(key) ? BLANK_STR : data.get(key)).append(AMPERSAND);
            }
        });
        context.append(PASSWORD).append(EQUAL_SIGN).append(password);
        return context.toString();
    }

    /**
     * @Description 验证签名信息
     * @param signature 接口提供的签名
     * @param timestamp 时间戳
     * @param validity 签名有效时间（秒）
     */
    public static boolean verifySignature(
            String api, String signature, String password, long timestamp, long validity,
            Map<String, Object> data) throws NoSuchAlgorithmException {
        notBlank(signature, "signature cannot be empty");
        long value = currentTimeMillis() / SECOND.getMillis() + ERROR_SECOND - timestamp;
        isTrue(value > ZERO, "timestamp error");
        isTrue(value < validity, "signature timeout");
        return signature.equals(digest(getPlaintext(api, data, password, timestamp)));
    }

    /**
     * 获取签名参数
     */
    public static Map<String,Object> getSignatureParameter(Object object) {
        return getSignatureParameters(null, object);
    }

    /**
     * 获取签名参数
     */
    public static Map<String,Object> getSignatureParameter(Map<String, Object> parameters) {
        return getSignatureParameter(null, parameters);
    }

    /**
     * 获取签名参数
     * @param fields 签名字段
     * @param parameters 参数列表
     */
    public static Map<String,Object> getSignatureParameter(List<String> fields, Map<String, Object> parameters) {
        if (isEmpty(parameters)) {
            return emptyMap();
        }
        Map<String, Object> results = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (isSignatureValue(fields, key, value)) {
                results.put(key, value);
            }
        });
        return results;
    }

    /**
     * 获取签名参数
     * @param fields 签名字段
     * @param object 数据对象
     */
    public static Map<String,Object> getSignatureParameters(List<String> fields, Object object) {
        if (null == object || object instanceof Object[] || object instanceof Collection) {
            return emptyMap();
        }
        return getSignatureParameters(fields, object.getClass(), object);
    }

    private static Map<String, Object> getSignatureParameters(
            List<String> fields, Class<?> clazz, Object object) {
        if (null == clazz || clazz.equals(Object.class)) {
            return emptyMap();
        }
        Field[] attributes = clazz.getDeclaredFields();
        if (isEmpty(attributes)) {
            return emptyMap();
        }

        Map<String, Object> results = new HashMap<>();
        for (Field attribute : attributes) {
            String name = attribute.getName();
            Object value = getValue(object, name);
            SignField signField = attribute.getAnnotation(SignField.class);
            name = null != signField ? signField.value() : name;
            if (isSignatureValue(fields, name, value)) {
                results.put(name, value);
            }
        }
        Map<String, Object> parameters = getSignatureParameters(fields, clazz.getSuperclass(), object);
        if (isNotEmpty(parameters)) {
            results.putAll(parameters);
        }
        return results;
    }

    /**
     * 判断字段是否用于签名
     */
    private static boolean isSignatureValue(List<String> fields, String name, Object value) {
        return isBasic(value) && (isEmpty(fields) || fields.contains(name));
    }
}
