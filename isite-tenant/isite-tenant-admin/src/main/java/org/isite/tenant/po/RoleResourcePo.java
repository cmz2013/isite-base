package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "role_resource")
//@AllArgsConstructor 不建议使用，因为字段顺序一旦调整，构造函数传参就会错误，如果类型匹配很容易忽略该错误
public class RoleResourcePo extends Po<Integer> {
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 资源ID
     */
    private Integer resourceId;

    public RoleResourcePo() {
        super();
    }

    public RoleResourcePo(Integer roleId, Integer resourceId) {
        super();
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
}
