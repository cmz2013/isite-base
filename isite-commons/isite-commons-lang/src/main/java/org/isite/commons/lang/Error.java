package org.isite.commons.lang;

import lombok.Getter;
/**
 * @Description 业务自定义异常
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class Error extends RuntimeException {
    /**
     * 返回状态码。如果业务自定义异常状态码，建议按系统进行区间划分
     */
    private final Integer code;

    public Error(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
