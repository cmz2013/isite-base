package org.isite.shop.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.commons.web.mq.Message;
import org.isite.commons.web.mq.Publisher;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.service.TradeOrderItemService;
import org.isite.shop.service.TradeOrderService;
import org.isite.shop.support.dto.PayNoticeDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.isite.commons.cloud.converter.Converter.groupBy;
import static org.isite.commons.lang.Constants.NEW_LINE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.operation.support.constants.OperationConstants.QUEUE_OPERATION_EVENT;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderItemSelectivePos;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderSelectivePo;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderSupplierDto;
import static org.isite.shop.support.constants.ShopConstants.EXCHANGE_TRADE_ORDER_SUCCESS;
import static org.isite.shop.support.enums.TradeStatus.NOTPAY;

/**
 * @Description 订单支付成功消息消费者，完成以下操作：
 * 1）更新订单状态。
 * 2）根据Spu供应商广播MQ消息。
 * 3）AOP形式发送运营消息。
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class PayNoticeConsumer implements Consumer<PayNoticeDto> {

    private TradeOrderService tradeOrderService;
    private TradeOrderItemService tradeOrderItemService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PayNoticeConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setTradeOrderService(TradeOrderService tradeOrderService) {
        this.tradeOrderService = tradeOrderService;
    }

    @Autowired
    public void setTradeOrderItemService(TradeOrderItemService tradeOrderItemService) {
        this.tradeOrderItemService = tradeOrderItemService;
    }

    /**
     * Spring Aspect事务默认最小优先级
     */
    @Override
    @Validated
    @Transactional(rollbackFor = Exception.class)
    @Publisher(messages = @Message(queues = QUEUE_OPERATION_EVENT, producer = PayNoticeProducer.class))
    public Basic handle(PayNoticeDto payNoticeDto) {
        try {
            TradeOrderPo tradeOrderPo = tradeOrderService.findOne(TradeOrderPo::getOrderNumber, payNoticeDto.getOrderNumber());
            if (null == tradeOrderPo) {
                log.error(toJsonString(payNoticeDto) + NEW_LINE +
                        "订单号 {} 不存在", payNoticeDto.getOrderNumber());
                return new Basic.Nack();
            }
            if (!tradeOrderPo.getTradeStatus().equals(NOTPAY)) {
                log.error(toJsonString(payNoticeDto) + NEW_LINE + "订单状态必须是未支付");
                return new Basic.Nack();
            }
            if (!tradeOrderPo.getPayPrice().equals(payNoticeDto.getPayPrice())) {
                log.error(toJsonString(payNoticeDto) + NEW_LINE +
                        "订单金额 {} 与支付金额 {} 不一致", tradeOrderPo.getPayPrice(), payNoticeDto.getPayPrice());
                return new Basic.Nack();
            }
            tradeOrderService.updateSelectiveById(toTradeOrderSelectivePo(tradeOrderPo.getId(), payNoticeDto));
            List<TradeOrderItemPo> orderItemPos = tradeOrderItemService.findList(TradeOrderItemPo::getOrderId, tradeOrderPo.getId());
            if (payNoticeDto.getServiceCharge() > ZERO) {
                toTradeOrderItemSelectivePos(tradeOrderPo.getPayPrice(), orderItemPos, payNoticeDto.getServiceCharge())
                        .forEach(tradeOrderItemService::updateSelectiveById);
            }
            groupBy(orderItemPos, TradeOrderItemPo::getSupplier).forEach((supplier, pos) -> rabbitTemplate.convertAndSend(
                    EXCHANGE_TRADE_ORDER_SUCCESS, supplier.getCode(), toTradeOrderSupplierDto(tradeOrderPo, pos)));
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(TRUE);
        }
    }
}
