package org.isite.commons.web.sign;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.encoder.Md5Encoder;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.utils.TypeUtils;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final int ERROR_SECOND = Constants.THREE;

    private SignUtils() {
    }

    /**
     * 获取签名加密串
     */
    @SneakyThrows
    public static String getSignature(String api, Map<String, Object> data, String password, long timestamp) {
        return Md5Encoder.digest(getPlaintext(api, data, password, timestamp));
    }

    /**
     * 获取签名明文
     */
    private static String getPlaintext(String api, Map<String, Object> data, String password, long timestamp) {
        List<String> keys = new ArrayList<>();
        data.forEach((key, value) -> {
            if (TypeUtils.isBasic(value)) {
                keys.add(key);
            }
        });

        keys.add(HttpHeaders.X_TIMESTAMP);
        Collections.sort(keys);
        StringBuilder context = new StringBuilder(api).append(Constants.AMPERSAND);
        keys.forEach(key -> {
            if (HttpHeaders.X_TIMESTAMP.equals(key)) {
                context.append(key).append(Constants.EQUAL_SIGN).append(timestamp).append(Constants.AMPERSAND);
            } else {
                context.append(key).append(Constants.EQUAL_SIGN).append(null == data.get(key) ?
                        Constants.BLANK_STR : data.get(key)).append(Constants.AMPERSAND);
            }
        });
        context.append(PASSWORD).append(Constants.EQUAL_SIGN).append(password);
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
        Assert.notBlank(signature, "signature cannot be empty");
        long value = System.currentTimeMillis() / ChronoUnit.SECOND.getMillis() + ERROR_SECOND - timestamp;
        Assert.isTrue(value > Constants.ZERO, "timestamp error");
        Assert.isTrue(value < validity, "signature timeout");
        return signature.equals(Md5Encoder.digest(getPlaintext(api, data, password, timestamp)));
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
        if (MapUtils.isEmpty(parameters)) {
            return Collections.emptyMap();
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
            return Collections.emptyMap();
        }
        return getSignatureParameters(fields, object.getClass(), object);
    }

    private static Map<String, Object> getSignatureParameters(
            List<String> fields, Class<?> clazz, Object object) {
        if (null == clazz || clazz.equals(Object.class)) {
            return Collections.emptyMap();
        }
        Field[] attributes = clazz.getDeclaredFields();
        if (ArrayUtils.isEmpty(attributes)) {
            return Collections.emptyMap();
        }
        Map<String, Object> results = new HashMap<>();
        for (Field attribute : attributes) {
            String name = attribute.getName();
            Object value = Reflection.getValue(object, name);
            SignField signField = attribute.getAnnotation(SignField.class);
            name = null != signField ? signField.value() : name;
            if (isSignatureValue(fields, name, value)) {
                results.put(name, value);
            }
        }
        Map<String, Object> parameters = getSignatureParameters(fields, clazz.getSuperclass(), object);
        if (MapUtils.isNotEmpty(parameters)) {
            results.putAll(parameters);
        }
        return results;
    }

    /**
     * 判断字段是否用于签名
     */
    private static boolean isSignatureValue(List<String> fields, String name, Object value) {
        return TypeUtils.isBasic(value) && (CollectionUtils.isEmpty(fields) || fields.contains(name));
    }
}
