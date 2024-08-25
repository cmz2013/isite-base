package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 租户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TenantDto extends Dto<Integer> {
    /**
     * 租户名称
     */
    @NotNull(groups = {Add.class, Update.class})
    private String name;
    /**
     * 联系人
     */
    @NotNull(groups = {Add.class, Update.class})
    private String contact;
    /**
     * 联系电话
     */
    @NotNull(groups = {Add.class, Update.class})
    private String phone;
    /**
     * 功能权限
     */
    @NotEmpty(groups = {Add.class, Update.class})
    private List<Integer> resourceIds;
    /**
     * 备注
     */
    private String remark;
}
