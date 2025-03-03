package org.isite.fms.data.constants;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class AlipayConstants {

    private AlipayConstants() {
    }

    /**
     * 商户订单号
     */
    public static final String FIELD_OUT_TRADE_NO = "out_trade_no";
    public static final String FIELD_TRADE_STATUS = "trade_status";
    /**
     * 交易支付时间
     */
    public static final String FIELD_GMT_PAYMENT = "gmt_payment";
    /**
     * 公用回传参数
     */
    public static final String FIELD_PASSBACK_PARAMS = "passback_params";
    /**
     * 支付宝交易号
     */
    public static final String FIELD_TRADE_NO = "trade_no";
    /**
     * 订单金额(元)
     */
    public static final String FIELD_TOTAL_AMOUNT = "total_amount";
    /**
     * 交易支付成功
     */
    public static final String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";
    /**
     * 交易结束，不可退款
     */
    public static final String TRADE_STATUS_FINISHED = "TRADE_FINISHED";
}
