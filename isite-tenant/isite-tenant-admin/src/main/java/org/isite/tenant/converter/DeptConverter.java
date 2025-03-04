package org.isite.tenant.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.tenant.data.dto.DepartmentDto;
import org.isite.tenant.po.DepartmentPo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DeptConverter {

    private DeptConverter() {
    }

    public static DepartmentPo toDeptPo(int tenantId, DepartmentDto departmentDto) {
        DepartmentPo departmentPo = DataConverter.convert(departmentDto, DepartmentPo::new);
        departmentPo.setTenantId(tenantId);
        departmentPo.setStatus(ActiveStatus.ENABLED);
        if (null == departmentPo.getRemark()) {
            departmentPo.setRemark(Constants.BLANK_STR);
        }
        if (null == departmentPo.getPid()) {
            departmentPo.setPid(Constants.ZERO);
        }
        return departmentPo;
    }

    public static DepartmentPo toDeptSelectivePo(DepartmentDto departmentDto) {
        DepartmentPo departmentPo = DataConverter.convert(departmentDto, DepartmentPo::new);
        if (null == departmentPo.getRemark()) {
            departmentPo.setRemark(Constants.BLANK_STR);
        }
        if (null == departmentPo.getPid()) {
            departmentPo.setPid(Constants.ZERO);
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
