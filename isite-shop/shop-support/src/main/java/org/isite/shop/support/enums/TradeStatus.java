package org.isite.shop.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum TradeStatus implements Enumerable<Integer> {
    /**
     * 未支付
     */
    NOTPAY(0),
    /**
     * 已支付，交易成功
     */
    SUCCESS(1),
    /**
     * 退款
     */
    REFUND(2);

    private final Integer code;

    TradeStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
