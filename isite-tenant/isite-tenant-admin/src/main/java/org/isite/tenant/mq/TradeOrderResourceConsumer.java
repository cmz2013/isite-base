package org.isite.tenant.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.commons.web.sms.SmsClient;
import org.isite.shop.support.dto.TradeOrderSupplierDto;
import org.isite.tenant.converter.TenantConverter;
import org.isite.tenant.data.vo.ResourceSaleParam;
import org.isite.tenant.po.TenantPo;
import org.isite.tenant.service.TenantService;
import org.isite.user.client.UserAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
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
            orderSupplierDto.getSkuDtos().forEach(skuDto -> {
                ResourceSaleParam saleParam = Jackson.parseObject(skuDto.getSupplierParam(), ResourceSaleParam.class);
                if (null == saleParam.getTenantId()) {
                    for (int i = Constants.ZERO; i < skuDto.getSkuNum(); i++) {
                        addTenant(orderSupplierDto.getUserId(), skuDto.getSpuName() +
                                        (i == Constants.ZERO ? Constants.BLANK_STR : Constants.UNDERLINE + (i + Constants.ONE)), saleParam);
                    }
                } else {
                    // 租户续期，skuDto.getSkuNum() 没有意义
                    updateTenant(saleParam);
                }
            });
            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(Boolean.TRUE);
        }
    }

    private void addTenant(long userId, String tenantName, ResourceSaleParam saleParam) {
        TenantPo tenantPo = TenantConverter.toTenantPo(UserAccessor.getUserDetails(userId), tenantName, saleParam.getExpireDays());
        tenantService.addTenant(userId, tenantPo, saleParam.getResourceIds());
        sendTenantOpenMessage(tenantPo.getPhone(), tenantName);
    }

    private void sendTenantOpenMessage(String phone, String tenant) {
        try {
            smsClient.send(phone, String.format(MessageUtils.getMessage("tenant.opened.message",
                    "Congratulations, you have successfully opened %s!"), tenant));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateTenant(ResourceSaleParam saleParam) {
        TenantPo tenantPo = new TenantPo();
        tenantPo.setId(saleParam.getTenantId());
        tenantPo.setExpireTime(LocalDateTime.now().plusDays(saleParam.getExpireDays()));
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
