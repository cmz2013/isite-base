package org.isite.shop.mq;

import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Producer;
import org.isite.operation.support.dto.EventDto;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.service.TradeOrderItemService;
import org.isite.shop.service.TradeOrderService;
import org.isite.shop.support.dto.PayNoticeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.support.enums.EventType.POST_SHOP_PAY_NOTIFY;

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
        Basic result = cast(returnValue);
        if (result.getClass().equals(Basic.Ack.class)) {
            EventDto eventDto = new EventDto();
            PayNoticeDto payNoticeDto = cast(args[ZERO]);
            TradeOrderPo tradeOrderPo = tradeOrderService.findOne(TradeOrderPo::getOrderNumber, payNoticeDto.getOrderNumber());
            eventDto.setUserId(tradeOrderPo.getUserId());
            eventDto.setEventType(POST_SHOP_PAY_NOTIFY);
            eventDto.setObjectValue(valueOf(tradeOrderPo.getOrderNumber()));
            //最近一年内累计消费金额(元)
            eventDto.setEventParam(tradeOrderItemService.sumLastYearPayPrice(tradeOrderPo.getUserId()) / 100);
            return eventDto;
        }
        return null;
    }
}
