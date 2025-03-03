package org.isite.shop.support.mq;

import org.isite.shop.support.constants.ShopConstants;
import org.springframework.amqp.core.DirectExchange;
/**
 * @Description 交换器（直接模式）：订单支付成功，广播消息。
 * 生产者通常只需要发送消息到Exchange，而不需要定义具体的队列。
 * @Author <font color='blue'>zhangcm</font>
 */
public class TradeOrderExchange extends DirectExchange {

    public TradeOrderExchange() {
        /*
         * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
         * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除
         */
        super(ShopConstants.EXCHANGE_TRADE_ORDER_SUCCESS, true, false);
    }
}
