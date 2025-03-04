package org.isite.user.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.shop.support.dto.TradeOrderSkuDto;
import org.isite.shop.support.dto.TradeOrderSupplierDto;
import org.isite.user.data.vo.VipSaleParam;
import org.isite.user.po.VipPo;
import org.isite.user.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class TradeOrderVipConsumer implements Consumer<TradeOrderSupplierDto> {

    private VipService vipService;

    @Override
    @Validated
    public Basic handle(TradeOrderSupplierDto supplierDto) {
        try {
            VipPo vipPo = vipService.findOne(VipPo::getUserId, supplierDto.getUserId());
            LocalDateTime now = LocalDateTime.now();
            if (null == vipPo) {
                vipPo = new VipPo(supplierDto.getUserId(), now);
            } else if (vipPo.getExpireTime().isBefore(now)) {
                vipPo.setExpireTime(now);
            }
            for (TradeOrderSkuDto skuDto : supplierDto.getSkuDtos()) {
                VipSaleParam vipSaleParam = Jackson.parseObject(skuDto.getSupplierParam(), VipSaleParam.class);
                vipPo.setExpireTime(vipPo.getExpireTime().plusDays(vipSaleParam.getExpireDays() * skuDto.getSkuNum()));
            }
            if (null == vipPo.getId()) {
                vipService.insert(vipPo);
            } else {
                vipService.updateById(vipPo);
            }
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(Boolean.TRUE);
        }
    }

    @Autowired
    public void setVipService(VipService vipService) {
        this.vipService = vipService;
    }
}
