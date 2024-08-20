package org.isite.commons.cloud.sign;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static org.isite.commons.cloud.utils.PropertyUtils.getProperty;
import static org.isite.commons.lang.Assert.notBlank;

/**
 * @Description 获取接口签名秘钥
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class SignSecret {
    private static final String SIGNATURE_SECRET_PREFIX = "security.signature.secret.";

    /**
     * @Description 更具serviceId获取签名秘钥
     */
    public String password(String serviceId) {
        String password = getProperty(SIGNATURE_SECRET_PREFIX + serviceId);
        notBlank(password, "password is required: " + SIGNATURE_SECRET_PREFIX + serviceId);
        return password;
    }
}
