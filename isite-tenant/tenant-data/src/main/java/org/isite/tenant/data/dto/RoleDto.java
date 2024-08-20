package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotBlank;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class RoleDto extends Dto<Integer> {
    /**
     * 角色名称
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String name;
    /**
     * 备注
     */
    private String remark;
}
