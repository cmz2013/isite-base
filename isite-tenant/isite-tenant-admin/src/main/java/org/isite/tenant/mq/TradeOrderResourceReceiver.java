package org.isite.tenant.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.shop.support.dto.TradeOrderSupplierDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.tenant.data.constants.TenantConstants.QUEUE_TRADE_ORDER_SUCCESS_TENANT_RESOURCE;

/**
 * @Description 用户完成订单支付，MQ方式给租户添加资源（功能权限）
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = QUEUE_TRADE_ORDER_SUCCESS_TENANT_RESOURCE)
public class TradeOrderResourceReceiver extends ReceiverWrapper<TradeOrderSupplierDto> {

    @Autowired
    public TradeOrderResourceReceiver(TradeOrderResourceConsumer consumer) {
        super(consumer);
    }
}
