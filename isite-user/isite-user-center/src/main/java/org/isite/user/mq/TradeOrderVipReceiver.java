package org.isite.user.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.shop.support.dto.TradeOrderSupplierDto;
import org.isite.user.data.constants.UserConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @Description 用户完成订单支付，MQ方式创建VIP会员
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = UserConstants.QUEUE_TRADE_ORDER_SUCCESS_USER_VIP)
public class TradeOrderVipReceiver extends ReceiverWrapper<TradeOrderSupplierDto> {

    @Autowired
    public TradeOrderVipReceiver(TradeOrderVipConsumer consumer) {
        super(consumer);
    }
}
