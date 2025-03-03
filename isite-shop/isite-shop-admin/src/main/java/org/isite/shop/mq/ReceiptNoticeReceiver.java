package org.isite.shop.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.fms.data.constants.FmsConstants;
import org.isite.fms.data.dto.ReceiptNoticeDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 订单收款成功消息接收者
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = FmsConstants.QUEUE_RECEIPT_SUCCESS)
public class ReceiptNoticeReceiver extends ReceiverWrapper<ReceiptNoticeDto> {

    @Autowired
    public ReceiptNoticeReceiver(ReceiptNoticeConsumer consumer) {
        super(consumer);
    }
}
