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
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static org.isite.commons.cloud.data.Converter.groupBy;
import static org.isite.commons.lang.data.Constants.NEW_LINE;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.operation.support.constants.OperationConstants.QUEUE_OPERATION_EVENT;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderSelectivePo;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderSupplierDto;
import static org.isite.shop.support.constants.ShopConstants.EXCHANGE_TRADE_ORDER_SUCCESS;

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
                        "订单号不存在：{}", payNoticeDto.getOrderNumber());
                return new Basic.Nack();
            }
            if (tradeOrderPo.getPayPrice() != payNoticeDto.getPayPrice()) {
                log.error(toJsonString(payNoticeDto) + NEW_LINE +
                        "订单金额与支付金额不一致: {} != {}", tradeOrderPo.getPayPrice(), payNoticeDto.getPayPrice());
                return new Basic.Nack();
            }
            tradeOrderService.updateSelectiveById(toTradeOrderSelectivePo(tradeOrderPo.getId(), payNoticeDto));
            Map<String, List<TradeOrderItemPo>> supplierMap = groupBy(tradeOrderItemService.findList(
                    TradeOrderItemPo::getOrderId, tradeOrderPo.getId()), TradeOrderItemPo::getSupplier);
            supplierMap.forEach((supplier, orderItemPos) -> rabbitTemplate.convertAndSend(
                    EXCHANGE_TRADE_ORDER_SUCCESS, supplier, toTradeOrderSupplierDto(tradeOrderPo, orderItemPos)));
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(TRUE);
        }
    }
}
