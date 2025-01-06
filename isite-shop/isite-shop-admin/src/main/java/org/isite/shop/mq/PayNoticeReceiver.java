package org.isite.shop.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.shop.support.dto.PayNoticeDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.shop.support.constants.ShopConstants.QUEUE_TRADE_ORDER_SUCCESS;

/**
 * @Description 订单支付成功消息接收者
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = QUEUE_TRADE_ORDER_SUCCESS)
public class PayNoticeReceiver extends ReceiverWrapper<PayNoticeDto> {

    @Autowired
    public PayNoticeReceiver(PayNoticeConsumer consumer) {
        super(consumer);
    }
}
