package org.isite.commons.lang.http;

import org.isite.commons.lang.enums.Enumerable;

/**
 * http status
 * @author <font color='blue'>zhangcm</font>
 */
public enum HttpStatus implements Enumerable<Integer> {
    /**
     * 成功
     */
    OK(200, "OK"),
    /**
     * 异常
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),
    /**
     * 禁止
     */
    FORBIDDEN(403, "Forbidden");

    private final int code;
    private final String reasonPhrase;

    HttpStatus(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}