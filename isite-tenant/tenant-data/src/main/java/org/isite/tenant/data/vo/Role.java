package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Role extends Vo<Integer> {
    /**
     * 角色名称
     */
    private String name;
    /**
     * 功能权限
     */
    private List<Resource> resources;
    /**
     * 备注
     */
    private String remark;
}
