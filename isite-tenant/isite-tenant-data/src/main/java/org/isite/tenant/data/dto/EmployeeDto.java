package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class EmployeeDto extends Dto<Long> {
    /**
     * 手机号, 用于关联userId
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String phone;
    /**
     * 员工域账号, 唯一
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String domainAccount;
    /**
     * 部门ID
     */
    @NotNull(groups = {Add.class, Update.class})
    private Integer deptId;
    /**
     * 试用期（月）
     */
    @NotNull(groups = {Add.class, Update.class})
    private Integer trialPeriod;
}
