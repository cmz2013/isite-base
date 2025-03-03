package org.isite.fms.data.constants;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class WxpayConstants {

    private WxpayConstants() {
    }

    /**
     * 商户系统内部订单号
     */
    public static final String FIELD_OUT_TRADE_NO = "out_trade_no";
    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_KEY = "key";
    public static final String FIELD_TRADE_STATE = "trade_state";
    /**
     * 支付完成时间，格式为yyyyMMddHHmmss
     */
    public static final String FIELD_TIME_END = "time_end";
    /**
     * 商家数据包，原样返回
     */
    public static final String FIELD_ATTACH = "attach";
    /**
     * 微信支付订单号
     */
    public static final String FIELD_TRANSACTION_ID = "transaction_id";
    /**
     * 订单总金额，单位为分
     */
    public static final String FIELD_TOTAL_FEE = "total_fee";
    /**
     * 表示交易已经成功支付
     */
    public static final String TRADE_STATE_SUCCESS = "SUCCESS";
    public static final String XML_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    public static final String XML_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[NG]]></return_msg></xml>";
}
