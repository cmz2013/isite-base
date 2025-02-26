package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.tenant.data.enums.OfficeStatus;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 员工信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "employee")
public class EmployeePo extends Po<Long> {
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
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private OfficeStatus officeStatus;
    /**
     * 入职日期
     */
    private Date hireDate;
    /**
     * 试用期（月）
     */
    private Integer trialPeriod;
    /**
     * 租户ID
     */
    private Integer tenantId;

}
