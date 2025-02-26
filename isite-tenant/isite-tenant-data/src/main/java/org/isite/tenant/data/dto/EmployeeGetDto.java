package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
import org.isite.tenant.data.enums.OfficeStatus;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class EmployeeGetDto extends Dto<Long> {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 员工域账号, 唯一
     */
    private String domainAccount;
    /**
     * 部门ID
     */
    private Integer deptId;
    /**
     * 员工岗位状态
     */
    private OfficeStatus officeStatus;
}
