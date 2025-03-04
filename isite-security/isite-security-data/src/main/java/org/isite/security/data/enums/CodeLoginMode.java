package org.isite.security.data.enums;

import org.isite.commons.lang.enums.Enumerable;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum CodeLoginMode implements Enumerable<String> {
    /**
     * 短信验证码
     */
    CAPTCHA_SMS,
    /**
     * 邮件验证码
     */
    CAPTCHA_EMAIL,
    /**
     * 微信授权码
     */
    CODE_WECHAT;

    @Override
    public String getCode() {
        return this.name();
    }
}
