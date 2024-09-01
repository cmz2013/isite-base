package org.isite.user.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.shop.support.dto.TradeOrderDto;
import org.isite.shop.support.dto.TradeOrderItemDto;
import org.isite.user.data.vo.VipSaleParam;
import org.isite.user.po.VipPo;
import org.isite.user.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static java.lang.Boolean.TRUE;
import static java.lang.System.currentTimeMillis;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.json.Jackson.parseObject;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class TradeOrderVipConsumer implements Consumer<TradeOrderDto> {

    private VipService vipService;

    @Override
    @Validated
    public Basic handle(TradeOrderDto tradeOrderDto) {
        try {
            VipPo vipPo = vipService.findOne(VipPo::getUserId, tradeOrderDto.getUserId());
            long expireTimeMillis = currentTimeMillis();
            if (null == vipPo) {
                vipPo = new VipPo(tradeOrderDto.getUserId(), new Date(expireTimeMillis));
            } else if (vipPo.getExpireTime().getTime() < expireTimeMillis) {
                vipPo.setExpireTime(new Date(expireTimeMillis));
            }
            for (TradeOrderItemDto orderItem : tradeOrderDto.getOrderItems()) {
                VipSaleParam vipSaleParam = parseObject(orderItem.getSupplierParam(), VipSaleParam.class);
                vipPo.setExpireTime(new Date(vipPo.getExpireTime().getTime() +
                        vipSaleParam.getExpireDays() * orderItem.getSkuCount() * DAY.getMillis()));
            }
            if (null == vipPo.getId()) {
                vipService.insert(vipPo);
            } else {
                vipService.updateById(vipPo);
            }
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(TRUE);
        }
    }

    @Autowired
    public void setVipService(VipService vipService) {
        this.vipService = vipService;
    }
}
