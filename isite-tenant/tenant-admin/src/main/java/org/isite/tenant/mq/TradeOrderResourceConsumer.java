package org.isite.tenant.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.commons.web.sms.SmsClient;
import org.isite.shop.support.dto.TradeOrderSupplierDto;
import org.isite.tenant.data.vo.ResourceSaleParam;
import org.isite.tenant.po.TenantPo;
import org.isite.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static java.lang.Boolean.TRUE;
import static java.lang.Long.max;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.UNDERLINE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.tenant.converter.TenantConverter.toTenantPo;
import static org.isite.user.client.UserAccessor.getUserDetails;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class TradeOrderResourceConsumer implements Consumer<TradeOrderSupplierDto> {

    private SmsClient smsClient;
    private TenantService tenantService;

    @Override
    @Validated
    public Basic handle(TradeOrderSupplierDto orderSupplierDto) {
        try {
            orderSupplierDto.getSkus().forEach(tradeOrderItemDto -> {
                ResourceSaleParam saleParam = parseObject(tradeOrderItemDto.getSupplierParam(), ResourceSaleParam.class);
                if (null == saleParam.getTenantId()) {
                    for (int i = ZERO; i < tradeOrderItemDto.getSkuCount(); i++) {
                        addTenant(tradeOrderItemDto.getSpuName() + (i == ZERO ? BLANK_STR : UNDERLINE + i),
                                orderSupplierDto.getUserId(), saleParam);
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
        TenantPo tenantPo = toTenantPo(getUserDetails(userId), tenantName,
                new Date(currentTimeMillis() + saleParam.getExpireDays() * DAY.getMillis()));
        tenantService.addTenant(userId, tenantPo, saleParam.getResourceIds());
        sendTenantOpenMessage(tenantPo.getPhone(), tenantName);
    }

    private void sendTenantOpenMessage(String phone, String tenant) {
        try {
            smsClient.send(phone, format(getMessage("tenant.opened.message",
                    "Congratulations, you have successfully opened %s!"), tenant));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateTenant(int skuCount, ResourceSaleParam saleParam) {
        TenantPo tenantPo = new TenantPo();
        tenantPo.setId(saleParam.getTenantId());
        tenantPo.setExpireTime(new Date(max(tenantPo.getExpireTime().getTime(), currentTimeMillis()) +
                skuCount * saleParam.getExpireDays() * DAY.getMillis()));
        tenantService.updateTenant(tenantPo, saleParam.getResourceIds());
    }

    @Autowired
    public void setSmsClient(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @Autowired
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }
}
