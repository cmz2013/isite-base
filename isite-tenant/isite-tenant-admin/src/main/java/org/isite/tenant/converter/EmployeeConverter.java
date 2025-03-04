package org.isite.tenant.converter;

import org.isite.commons.lang.Constants;
import org.isite.tenant.data.dto.EmployeeDto;
import org.isite.tenant.data.enums.OfficeStatus;
import org.isite.tenant.po.EmployeePo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class EmployeeConverter {

    private EmployeeConverter() {
    }

    public static EmployeePo toEmployeePo(long userId, int tenantId) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setUserId(userId);
        employeePo.setOfficeStatus(OfficeStatus.NORMAL);
        employeePo.setTenantId(tenantId);
        employeePo.setDeptId(Constants.ZERO);
        employeePo.setDomainAccount(Constants.BLANK_STR);
        return employeePo;
    }

    public static EmployeePo toEmployeePo(int tenantId, EmployeeDto employeeDto, long userId) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setId(employeeDto.getId());
        employeePo.setDeptId(employeeDto.getDeptId());
        employeePo.setDomainAccount(employeeDto.getDomainAccount());
        employeePo.setUserId(userId);
        employeePo.setOfficeStatus(OfficeStatus.NORMAL);
        employeePo.setTenantId(tenantId);
        return employeePo;
    }

    public static EmployeePo toEmployeeSelectivePo(int tenantId, EmployeeDto employeeDto, long userId) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setId(employeeDto.getId());
        employeePo.setDeptId(employeeDto.getDeptId());
        employeePo.setDomainAccount(employeeDto.getDomainAccount());
        employeePo.setUserId(userId);
        employeePo.setTenantId(tenantId);
        return employeePo;
    }

    public static EmployeePo toEmployeeSelectivePo(int tenantId, long userId) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setUserId(userId);
        employeePo.setTenantId(tenantId);
        return employeePo;
    }
}
