package org.isite.tenant.converter;

import org.isite.tenant.data.dto.EmployeeDto;
import org.isite.tenant.po.EmployeePo;

import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.tenant.data.enums.OfficeStatus.NORMAL;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class EmployeeConverter {

    private EmployeeConverter() {
    }

    public static EmployeePo toEmployeePo(long userId, int tenantId) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setUserId(userId);
        employeePo.setOfficeStatus(NORMAL);
        employeePo.setTenantId(tenantId);
        employeePo.setDeptId(ZERO);
        employeePo.setDomainAccount(BLANK_STRING);
        return employeePo;
    }

    public static EmployeePo toEmployeePo(int tenantId, EmployeeDto employeeDto, long userId) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setId(employeeDto.getId());
        employeePo.setDeptId(employeeDto.getDeptId());
        employeePo.setDomainAccount(employeeDto.getDomainAccount());
        employeePo.setUserId(userId);
        employeePo.setOfficeStatus(NORMAL);
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

    public static EmployeePo toEmployeeSelectivePo(int tenantId, String domainAccount) {
        EmployeePo employeePo = new EmployeePo();
        employeePo.setTenantId(tenantId);
        employeePo.setDomainAccount(domainAccount);
        return employeePo;
    }
}
