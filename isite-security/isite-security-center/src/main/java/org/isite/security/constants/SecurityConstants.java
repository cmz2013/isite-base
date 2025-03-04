package org.isite.security.constants;

import org.isite.commons.lang.Constants;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String AUTHORIZATION_REQUEST = "authorizationRequest";
    public static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
    public static final String CLIENT_ID = "client_id";
    public static final String CODE_LOGIN_MODE = "code_login_mode";

    public static final String CAPTCHA_INFO = "${code} is your verification code, " +
            "please complete the verification within ${validity} minutes. " +
            "For your safety, do not leak easily.";
    /**
     * 验证码有效期:5分钟
     */
    public static final int CAPTCHA_VALIDITY = Constants.FIVE;
    /**
     * 验证码长度
     */
    public static final int CAPTCHA_LENGTH = Constants.SIX;
    /**
     * 一日内连续登陆失败次数最大值
     */
    public static final long LOGIN_FAILURE_TIMES_MAX = Constants.THREE;
}
