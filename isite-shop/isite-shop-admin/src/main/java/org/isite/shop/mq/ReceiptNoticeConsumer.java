package org.isite.shop.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.commons.web.mq.Message;
import org.isite.commons.web.mq.Publisher;
import org.isite.fms.data.dto.ReceiptNoticeDto;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.shop.converter.TradeOrderConverter;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.service.TradeOrderItemService;
import org.isite.shop.service.TradeOrderService;
import org.isite.shop.support.enums.TradeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
/**
 * @Description 订单收款成功消息消费者，完成以下操作：
 * 1）更新订单状态。
 * 2）根据Spu供应商广播MQ消息。
 * 3）AOP形式发送运营消息。
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class ReceiptNoticeConsumer implements Consumer<ReceiptNoticeDto> {

    private TradeOrderService tradeOrderService;
    private TradeOrderItemService tradeOrderItemService;

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
    @Publisher(messages = @Message(queues = OperationConstants.QUEUE_OPERATION_EVENT, producer = PaymentNoticeProducer.class))
    public Basic handle(ReceiptNoticeDto receiptNoticeDto) {
        try {
            TradeOrderPo tradeOrderPo = tradeOrderService.findOne(TradeOrderPo::getOrderNo, receiptNoticeDto.getOrderNo());
            if (null == tradeOrderPo) {
                log.error(Jackson.toJsonString(receiptNoticeDto) + Constants.NEW_LINE +
                        "订单号 {} 不存在", receiptNoticeDto.getOrderNo());
                return new Basic.Nack();
            }
            if (!tradeOrderPo.getStatus().equals(TradeStatus.PENDING)) {
                log.error(Jackson.toJsonString(receiptNoticeDto) + Constants.NEW_LINE + "订单状态必须是待支付");
                return new Basic.Nack();
            }
            if (!tradeOrderPo.getPayPrice().equals(receiptNoticeDto.getPayPrice())) {
                log.error(Jackson.toJsonString(receiptNoticeDto) + Constants.NEW_LINE +
                        "订单金额 {} 与支付金额 {} 不一致", tradeOrderPo.getPayPrice(), receiptNoticeDto.getPayPrice());
                return new Basic.Nack();
            }
            tradeOrderService.updateSelectiveById(TradeOrderConverter.toTradeOrderSelectivePo(tradeOrderPo.getId(), receiptNoticeDto));
            List<TradeOrderItemPo> orderItemPos = tradeOrderItemService.findList(TradeOrderItemPo::getOrderId, tradeOrderPo.getId());
            if (receiptNoticeDto.getServiceCharge() > Constants.ZERO) {
                TradeOrderConverter.toTradeOrderItemSelectivePos(tradeOrderPo.getPayPrice(), orderItemPos, receiptNoticeDto.getServiceCharge())
                        .forEach(tradeOrderItemService::updateSelectiveById);
            }
            tradeOrderService.sendOrderSuccessMessage(tradeOrderPo, orderItemPos);
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(Boolean.TRUE);
        }
    }
}
