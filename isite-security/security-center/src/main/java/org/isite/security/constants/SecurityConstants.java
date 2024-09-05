package org.isite.security.constants;

import static org.isite.commons.lang.Constants.FIVE;
import static org.isite.commons.lang.Constants.SIX;
import static org.isite.commons.lang.Constants.THREE;

/**
 * @author : zhangcm
 */
public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String AUTHORIZATION_REQUEST = "authorizationRequest";
    public static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
    public static final String CLIENT_ID = "client_id";
    /**
     * 授权范围 rbac：基于角色的权限访问控制
     */
    public static final String AUTHORIZATION_SCOPE_RBAC = "rbac";
    /**
     * 验证码有效期:5分钟
     */
    public static final int VERIFY_CODE_VALIDITY = FIVE;
    /**
     * 验证码长度
     */
    public static final int VERIFY_CODE_LENGTH = SIX;
    /**
     * 一日内连续登陆失败次数最大值
     */
    public static final long LOGIN_FAILURE_TIMES_MAX = THREE;
}
