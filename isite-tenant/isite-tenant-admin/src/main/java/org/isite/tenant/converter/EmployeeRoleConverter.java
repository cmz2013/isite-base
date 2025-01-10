package org.isite.tenant.converter;

import org.isite.tenant.po.EmployeeRolePo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class EmployeeRoleConverter {

    private EmployeeRoleConverter() {
    }

    public static EmployeeRolePo toEmployeeRoleSelectivePo(int employeeId, int roleId) {
        EmployeeRolePo employeeRolePo = new EmployeeRolePo();
        employeeRolePo.setEmployeeId(employeeId);
        employeeRolePo.setRoleId(roleId);
        return employeeRolePo;
    }
}
