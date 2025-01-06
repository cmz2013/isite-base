package org.isite.commons.web.sign;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static org.isite.commons.cloud.utils.PropertyUtils.getProperty;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class SignSecret {
    private static final String PROPERTY_SIGNATURE_SECRET_PREFIX = "security.signature.secret.";

    /**
     * @Description 根据serviceId获取密码，用于生成签名
     */
    public String password(String serviceId) {
        return getProperty(PROPERTY_SIGNATURE_SECRET_PREFIX + "password." + serviceId);
    }

    /**
     * @Description 根据serviceId获取签名，主要用于第三方接口签名验证。password优先级更高，如果没有配置password，则使用apiKey
     */
    public String apiKey(String serviceId) {
        return getProperty(PROPERTY_SIGNATURE_SECRET_PREFIX + "apiKey." + serviceId);
    }
}
