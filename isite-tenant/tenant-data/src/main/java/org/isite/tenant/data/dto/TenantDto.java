package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotNull;

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
     * 启用/停用
     */
    private SwitchStatus status;
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
     * 备注
     */
    private String remark;
}
