package org.isite.shop.mq;

import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Producer;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.EventType;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.service.TradeOrderItemService;
import org.isite.shop.service.TradeOrderService;
import org.isite.shop.support.dto.PayNoticeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PayNoticeProducer implements Producer {

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

    @Override
    public Object getBody(Object[] args, Object returnValue) {
        Basic basic = TypeUtils.cast(returnValue);
        if (basic instanceof Basic.Ack) {
            PayNoticeDto payNoticeDto = TypeUtils.cast(args[Constants.ZERO]);
            EventDto eventDto = new EventDto();
            TradeOrderPo tradeOrderPo = tradeOrderService.findOne(TradeOrderPo::getOrderNo, payNoticeDto.getOrderNo());
            eventDto.setUserId(tradeOrderPo.getUserId());
            eventDto.setEventType(EventType.POST_SHOP_PAY_NOTIFY);
            eventDto.setObjectValue(String.valueOf(tradeOrderPo.getOrderNo()));
            //最近一年内累计消费金额(元)
            eventDto.setEventParam(tradeOrderItemService.sumLastYearPayPrice(tradeOrderPo.getUserId()) / Constants.HUNDRED);
            return eventDto;
        }
        return null;
    }
}
