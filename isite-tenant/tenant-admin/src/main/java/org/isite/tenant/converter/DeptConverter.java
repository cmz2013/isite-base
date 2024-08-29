package org.isite.tenant.converter;

import org.isite.tenant.data.dto.DeptDto;
import org.isite.tenant.po.DeptPo;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;
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
        deptPo.setStatus(ENABLED);
        if (null == deptPo.getRemark()) {
            deptPo.setRemark(BLANK_STRING);
        }
        if (null == deptPo.getPid()) {
            deptPo.setPid(ZERO);
        }
        return deptPo;
    }

    public static DeptPo toDeptSelectivePo(DeptDto deptDto) {
        DeptPo deptPo = convert(deptDto, DeptPo::new);
        if (null == deptPo.getRemark()) {
            deptPo.setRemark(BLANK_STRING);
        }
        if (null == deptPo.getPid()) {
            deptPo.setPid(ZERO);
        }
        return deptPo;
    }
}
