package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "employee_role")
//@AllArgsConstructor 不建议使用，因为字段顺序一旦调整，构造函数传参就会错误，如果类型匹配很容易忽略该错误
public class EmployeeRolePo extends Po<Integer> {
    /**
     * 员工ID
     */
    private Integer employeeId;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 租户ID
     */
    private Integer tenantId;

    public EmployeeRolePo() {
        super();
    }

    public EmployeeRolePo(Integer employeeId, Integer roleId, Integer tenantId) {
        super();
        this.employeeId = employeeId;
        this.roleId = roleId;
        this.tenantId = tenantId;
    }
}
