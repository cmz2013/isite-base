package org.isite.security.data.enums;

import org.isite.commons.lang.enums.Enumerable;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;


/**
 * @Description 验证码发送方式
 * @Author <font color='blue'>zhangcm</font>
 */
public enum VerifyCodeMode implements Enumerable<String> {
    /**
     * 手机短信验证码
     */
    SMS("VerifyCode.sms", "phone number"),
    /**
     * Email验证码
     */
    EMAIL("VerifyCode.email", "email address");

    private final String labelKey;
    private final String defaultLabel;

    VerifyCodeMode(String labelKey, String defaultLabel) {
        this.labelKey = labelKey;
        this.defaultLabel = defaultLabel;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    public String getLabel() {
        return getMessage(labelKey, defaultLabel);
    }
}
