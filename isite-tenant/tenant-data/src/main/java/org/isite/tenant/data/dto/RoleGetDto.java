package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class RoleGetDto extends Dto<Integer> {
    /**
     * 角色名称
     */
    private String roleName;

}
