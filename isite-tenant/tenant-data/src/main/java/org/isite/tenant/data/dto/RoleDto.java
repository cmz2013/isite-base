package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.THOUSAND;

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
    private String roleName;
    /**
     * 功能权限
     */
    @Size(min = ONE, max = THOUSAND, groups = {Add.class, Update.class})
    private List<Integer> resourceIds;
    /**
     * 备注
     */
    private String remark;
}
