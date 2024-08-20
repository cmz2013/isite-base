package org.isite.tenant.converter;

import org.isite.tenant.data.dto.DeptDto;
import org.isite.tenant.po.DeptPo;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DeptConverter {

    private DeptConverter() {
    }

    public static DeptPo toDeptPo(DeptDto deptDto) {
        DeptPo deptPo = convert(deptDto, DeptPo::new);
        deptPo.setTenantId(getTenantId());
        return deptPo;
    }
}
