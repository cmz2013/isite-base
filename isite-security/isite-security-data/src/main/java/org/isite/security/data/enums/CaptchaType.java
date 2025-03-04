package org.isite.security.data.enums;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.enums.Enumerable;
/**
 * @Description 验证码类型
 * @Author <font color='blue'>zhangcm</font>
 */
public enum CaptchaType implements Enumerable<String> {
    /**
     * 短信验证码
     */
    SMS("captcha.agent.sms", "phone number"),
    /**
     * 邮件验证码
     */
    EMAIL("captcha.agent.email", "email address");

    private final String agentKey;
    private final String agentLabel;

    CaptchaType(String agentKey, String agentLabel) {
        this.agentKey = agentKey;
        this.agentLabel = agentLabel;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    public String getAgentLabel() {
        return MessageUtils.getMessage(agentKey, agentLabel);
    }
}
