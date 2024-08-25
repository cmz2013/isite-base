package org.isite.tenant.converter;

import org.isite.tenant.data.dto.TenantDto;
import org.isite.tenant.po.TenantPo;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TenantConverter {

    private TenantConverter() {
    }

    public static TenantPo toTenantPo(TenantDto tenantDto) {
        TenantPo tenantPo = convert(tenantDto, TenantPo::new);
        tenantPo.setStatus(ENABLED);
        if (null == tenantPo.getRemark()) {
            tenantPo.setRemark(BLANK_STRING);
        }
        return tenantPo;
    }

    public static TenantPo toTenantSelectivePo(TenantDto tenantDto) {
        TenantPo tenantPo = convert(tenantDto, TenantPo::new);
        if (null == tenantPo.getRemark()) {
            tenantPo.setRemark(BLANK_STRING);
        }
        return tenantPo;
    }
}
