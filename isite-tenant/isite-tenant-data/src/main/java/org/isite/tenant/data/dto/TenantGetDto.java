package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.ActiveStatus;

/**
 * @Description 租户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TenantGetDto {
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 启用/停用
     */
    private ActiveStatus status;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 备注
     */
    private String remark;
}
