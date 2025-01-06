package org.isite.security.data.enums;

import org.isite.commons.lang.enums.Enumerable;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;


/**
 * @Description 验证码类型
 * @Author <font color='blue'>zhangcm</font>
 */
public enum VerificationCodeType implements Enumerable<String> {
    /**
     * 短信验证码
     */
    SMS("verificationCode.agent.sms", "phone number"),
    /**
     * 邮件验证码
     */
    EMAIL("verificationCode.agent.email", "email address");

    private final String agentKey;
    private final String agentLabel;

    VerificationCodeType(String agentKey, String agentLabel) {
        this.agentKey = agentKey;
        this.agentLabel = agentLabel;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    public String getAgentLabel() {
        return getMessage(agentKey, agentLabel);
    }
}
