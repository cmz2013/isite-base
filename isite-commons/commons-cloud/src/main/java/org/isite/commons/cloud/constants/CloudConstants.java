package org.isite.commons.cloud.constants;
/**
 * @Description 自定义的请求头字段以x-打头
 * @Author <font color='blue'>zhangcm</font>
 */
public class CloudConstants {

    private CloudConstants() {
    }

    /**
     * 如果在请求头设置feign-sign-password，该拦截器自动设置签名信息。
     */
    public static final String FEIGN_SIGN_PASSWORD = "feign-sign-password";

    /**
     * 服务版本号，主要用于灰度发布：将部分流量引导到新版本的服务，同时保持大部分流量在旧版本上，从而进行逐步验证和测试
     */
    public static final String X_VERSION = "x-version";
    /**
     * 接口签名appCode
     */
    public static final String X_APP_CODE = "x-app-code";
    /**
     * 接口签名字段
     */
    public static final String X_SIGNATURE = "x-signature";
    /**
     * 接口签名时间戳：秒
     */
    public static final String X_TIMESTAMP = "x-timestamp";
    /**
     * 当前登录的用户ID
     */
    public static final String X_USER_ID = "x-user-id";
    /**
     * 当前登录的员工ID
     */
    public static final String X_EMPLOYEE_ID = "x-employee-id";
    /**
     * 当前登录员工的租户ID
     */
    public static final String X_TENANT_ID = "x-tenant-id";
    /**
     * 当前登录用户的客户端ID
     */
    public static final String X_CLIENT_ID = "x-client-id";
}
