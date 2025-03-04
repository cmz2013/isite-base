package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.lang.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
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
    private String tenantName;
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
    @Size(min = Constants.ONE, max = Constants.THOUSAND, groups = {Add.class, Update.class})
    private List<Integer> resourceIds;
    /**
     * 备注
     */
    private String remark;
    /**
     * 到期时间
     */
    @NotNull(groups = {Add.class, Update.class})
    private Date expireTime;
}
