package org.isite.tenant.converter;

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
}
