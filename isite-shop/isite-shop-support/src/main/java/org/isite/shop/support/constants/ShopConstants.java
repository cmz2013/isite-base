package org.isite.shop.support.constants;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ShopConstants {

    private ShopConstants() {
    }

    /**
     * 服务ID
     */
    public static final String SERVICE_ID = "isite-shop-admin";

    public static final String QUEUE_TRADE_ORDER_SUCCESS = "queue-trade-order-success";
    /**
     * 交换器（直接模式）：订单支付成功，广播消息。
     * 生产者通常只需要发送消息到Exchange，而不需要定义具体的队列。
     */
    public static final String EXCHANGE_TRADE_ORDER_SUCCESS = "exchange-trade-order-success";
}
