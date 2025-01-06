package org.isite.shop.support.enums;

import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum PaymentType implements Enumerable<Integer> {

    WXPAY(1, "微信"),
    ALIPAY(2, "支付宝");

    private final Integer code;
    @Getter
    private final String label;

    PaymentType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
