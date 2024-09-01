package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.TreeDto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DepartmentDto extends TreeDto<Integer> {
    /**
     * 部门名称
     */
    @NotNull(groups = {Add.class, Update.class})
    private String departmentName;
    /**
     * 备注
     */
    private String remark;
}
