package org.isite.tenant.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.tenant.data.dto.TenantDto;
import org.isite.tenant.po.TenantPo;
import org.isite.user.data.vo.UserDetails;

import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TenantConverter {

    private TenantConverter() {
    }

    public static TenantPo toTenantPo(TenantDto tenantDto) {
        TenantPo tenantPo = DataConverter.convert(tenantDto, TenantPo::new);
        tenantPo.setStatus(ActiveStatus.ENABLED);
        if (null == tenantPo.getRemark()) {
            tenantPo.setRemark(Constants.BLANK_STR);
        }
        return tenantPo;
    }

    public static TenantPo toTenantPo(UserDetails userDetails, String tenantName, long expireDays) {
        TenantPo tenantPo = new TenantPo();
        tenantPo.setTenantName(tenantName);
        tenantPo.setContact(userDetails.getRealName());
        tenantPo.setPhone(userDetails.getPhone());
        tenantPo.setExpireTime(LocalDateTime.now().plusDays(expireDays));
        tenantPo.setStatus(ActiveStatus.ENABLED);
        return tenantPo;
    }

    public static TenantPo toTenantSelectivePo(TenantDto tenantDto) {
        TenantPo tenantPo = DataConverter.convert(tenantDto, TenantPo::new);
        if (null == tenantPo.getRemark()) {
            tenantPo.setRemark(Constants.BLANK_STR);
        }
        return tenantPo;
    }
}
