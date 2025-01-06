package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;
import org.isite.commons.lang.enums.SwitchStatus;

import java.util.Date;

/**
 * @Description 租户信息
 * 1）内置根用户root创建租户、租户超管角色及权限（ROLE_ADMINISTRATOR）
 * 2）租户不可以修改和编辑超管角色及权限（ROLE_ADMINISTRATOR）
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Tenant extends Vo<Integer> {
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 启用/停用
     */
    private SwitchStatus status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 到期时间
     */
    private Date expireTime;
}
