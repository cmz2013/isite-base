package org.isite.commons.cloud.data.constants;

/**
 * @Description Http Header 常量类.自定义的请求头字段以x-打头
 * @Author <font color='blue'>zhangcm</font>
 */
public class HttpHeaders {

    private HttpHeaders() {}

    public static final String USER_AGENT = "User-Agent";
    public  static final String CONNECTION = "Connection";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String X_FORWARDED_HOST = "x-forwarded-host";
    public static final String AUTHORIZATION = "Authorization";
    /**
     * 用于识别协议（HTTP 或 HTTPS）
     */
    public static final String X_FORWARDED_PROTO = "x-forwarded-proto";
    public static final String X_FORWARDED_PREFIX = "x-forwarded-prefix";


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