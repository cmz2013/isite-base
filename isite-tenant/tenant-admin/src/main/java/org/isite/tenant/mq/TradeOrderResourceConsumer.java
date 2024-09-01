package org.isite.tenant.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.shop.support.dto.TradeOrderDto;
import org.isite.tenant.data.vo.ResourceSaleParam;
import org.isite.tenant.po.TenantPo;
import org.isite.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static java.lang.Boolean.TRUE;
import static java.lang.Long.max;
import static java.lang.System.currentTimeMillis;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.data.Constants.UNDERLINE;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.tenant.converter.TenantConverter.toTenantPo;
import static org.isite.user.client.UserAccessor.getUser;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class TradeOrderResourceConsumer implements Consumer<TradeOrderDto> {

    private TenantService tenantService;

    @Override
    @Validated
    public Basic handle(TradeOrderDto tradeOrderDto) {
        try {
            tradeOrderDto.getOrderItems().forEach(tradeOrderItemDto -> {
                ResourceSaleParam saleParam = parseObject(tradeOrderItemDto.getSupplierParam(), ResourceSaleParam.class);
                if (null == saleParam.getTenantId()) {
                    for (int i = ZERO; i < tradeOrderItemDto.getSkuCount(); i++) {
                        String tenantName = tradeOrderItemDto.getSkuName() + (i == ZERO ? BLANK_STRING : UNDERLINE + i);
                        addTenant(tenantName, tradeOrderDto.getUserId(), saleParam);
                    }
                } else {
                    updateTenant(tradeOrderItemDto.getSkuCount(), saleParam);
                }
            });
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(TRUE);
        }
    }

    private void addTenant(String tenantName, long userId, ResourceSaleParam saleParam) {
        TenantPo tenantPo = toTenantPo(getUser(userId), tenantName,
                new Date(currentTimeMillis() + saleParam.getExpireDays() * DAY.getMillis()));
        tenantService.addTenant(userId, tenantPo, saleParam.getResourceIds());
    }

    private void updateTenant(int skuCount, ResourceSaleParam saleParam) {
        TenantPo tenantPo = new TenantPo();
        tenantPo.setId(saleParam.getTenantId());
        tenantPo.setExpireTime(new Date(max(tenantPo.getExpireTime().getTime(), currentTimeMillis()) +
                skuCount * saleParam.getExpireDays() * DAY.getMillis()));
        tenantService.updateTenant(tenantPo, saleParam.getResourceIds());
    }

    @Autowired
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }
}
