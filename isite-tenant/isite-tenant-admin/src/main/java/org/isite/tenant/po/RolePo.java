package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Description 角色信息PO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "role")
public class RolePo extends Po<Integer> {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 租户ID
     */
    private Integer tenantId;
    /**
     * 备注
     */
    private String remark;
}
