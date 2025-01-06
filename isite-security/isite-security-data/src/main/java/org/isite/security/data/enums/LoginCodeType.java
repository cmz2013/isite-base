package org.isite.security.data.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum LoginCodeType implements Enumerable<String> {
    /**
     * 短信验证码
     */
    VERIFICATION_CODE_SMS,
    /**
     * 邮件验证码
     */
    VERIFICATION_CODE_EMAIL,
    /**
     * 微信授权码
     */
    AUTHORIZATION_CODE_WECHAT;

    @Override
    public String getCode() {
        return this.name();
    }
}
