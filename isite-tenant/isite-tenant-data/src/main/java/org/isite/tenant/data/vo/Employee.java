package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;
import org.isite.tenant.data.enums.OfficeStatus;

import java.time.LocalDate;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Employee extends Vo<Long> {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 员工域账号
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
    /**
     * 入职日期
     */
    private LocalDate hireDate;
    /**
     * 试用期（月）
     */
    private Integer trialPeriod;
}
