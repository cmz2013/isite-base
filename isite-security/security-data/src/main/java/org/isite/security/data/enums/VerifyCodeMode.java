package org.isite.security.data.enums;

import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;


/**
 * @Description 验证码发送方式
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public enum VerifyCodeMode implements Enumerable<String> {
    /**
     * 手机短信验证码
     */
    SMS(getMessage("VerifyCode.sms", "phone number")),
    /**
     * Email验证码
     */
    EMAIL(getMessage("VerifyCode.email", "email address"));

    private final String label;

    VerifyCodeMode(String label) {
        this.label = label;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
