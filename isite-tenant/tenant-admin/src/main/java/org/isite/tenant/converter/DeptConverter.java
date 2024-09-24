package org.isite.tenant.converter;

import org.isite.tenant.data.dto.DepartmentDto;
import org.isite.tenant.po.DepartmentPo;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DeptConverter {

    private DeptConverter() {
    }

    public static DepartmentPo toDeptPo(int tenantId, DepartmentDto departmentDto) {
        DepartmentPo departmentPo = convert(departmentDto, DepartmentPo::new);
        departmentPo.setTenantId(tenantId);
        departmentPo.setStatus(ENABLED);
        if (null == departmentPo.getRemark()) {
            departmentPo.setRemark(BLANK_STRING);
        }
        if (null == departmentPo.getPid()) {
            departmentPo.setPid(ZERO);
        }
        return departmentPo;
    }

    public static DepartmentPo toDeptSelectivePo(DepartmentDto departmentDto) {
        DepartmentPo departmentPo = convert(departmentDto, DepartmentPo::new);
        if (null == departmentPo.getRemark()) {
            departmentPo.setRemark(BLANK_STRING);
        }
        if (null == departmentPo.getPid()) {
            departmentPo.setPid(ZERO);
        }
        return departmentPo;
    }

    public static DepartmentPo toDeptSelectivePo(int tenantId, String deptName) {
        DepartmentPo departmentPo = new DepartmentPo();
        departmentPo.setTenantId(tenantId);
        departmentPo.setDepartmentName(deptName);
        return departmentPo;
    }
}
