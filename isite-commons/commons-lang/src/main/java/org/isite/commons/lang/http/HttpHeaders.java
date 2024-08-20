package org.isite.commons.lang.http;

/**
 * @Description Http Header 常量类
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

}