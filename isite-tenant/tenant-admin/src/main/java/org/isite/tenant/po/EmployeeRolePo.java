package org.isite.tenant.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "employee_role")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRolePo extends Po<Integer> {
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 角色ID
     */
    private Integer roleId;
}
