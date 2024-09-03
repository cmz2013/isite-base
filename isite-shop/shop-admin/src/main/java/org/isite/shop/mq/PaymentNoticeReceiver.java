package org.isite.shop.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.shop.support.constants.ShopConstants.QUEUE_TRADE_ORDER_SUCCESS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = QUEUE_TRADE_ORDER_SUCCESS)
public class PaymentNoticeReceiver extends ReceiverWrapper<Object> {

    @Autowired
    public PaymentNoticeReceiver(PaymentNoticeConsumer consumer) {
        super(consumer);
    }
}
