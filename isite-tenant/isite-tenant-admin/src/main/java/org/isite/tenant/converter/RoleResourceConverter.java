package org.isite.tenant.converter;

import org.isite.tenant.po.RoleResourcePo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class RoleResourceConverter {

    private RoleResourceConverter() {
    }

    public static List<RoleResourcePo> toRoleResourcePos(int roleId, List<Integer> resourceIds) {
        List<RoleResourcePo> poList = new ArrayList<>();
        resourceIds.forEach(resourceId -> poList.add(new RoleResourcePo(roleId, resourceId)));
        return poList;
    }
}
