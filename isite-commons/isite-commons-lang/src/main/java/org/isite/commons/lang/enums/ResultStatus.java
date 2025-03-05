package org.isite.commons.lang.enums;

import lombok.Getter;
/**
 * @Description http status
 * @Author <font color='blue'>zhangcm</font>
 */
public enum ResultStatus implements Enumerable<Integer> {
    /**
     * 成功
     */
    OK(200, "OK"),
    /**
     * 非法请求
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * 异常
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),
    /**
     * 禁止
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private final int code;
    @Getter
    private final String reasonPhrase;

    ResultStatus(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}