package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @Description 基于角色的访问控制
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Rbac {
    /**
     * 租户ID
     */
    private Tenant tenant;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 角色功能权限列表
     */
    private List<Role> roles;
    /**
     * 数据接口权限列表
     */
    private Set<DataApi> dataApis;
}
