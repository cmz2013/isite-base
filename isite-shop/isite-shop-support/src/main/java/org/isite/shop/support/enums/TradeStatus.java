package org.isite.shop.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum TradeStatus implements Enumerable<Integer> {
    /**
     * 待支付（订单必须在24小时内完成支付，否则24小时候关闭支付入口，显示支付已超时，定时任务在25小时后将订单状态更新为交易关闭，回退已售出数量 TODO）
     */
    PENDING(0),
    /**
     * 交易关闭
     */
    CLOSED(1),
    /**
     * 已支付，交易成功
     */
    SUCCESS(2),
    /**
     * 已退款
     */
    REFUND(3);

    private final Integer code;

    TradeStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
