package org.isite.tenant.converter;

import org.isite.tenant.data.dto.RoleDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.po.RolePo;

import java.util.List;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class RoleConverter {

    private RoleConverter() {
    }

    public static RolePo toRolePo(RoleDto roleDto) {
        if (null == roleDto) {
            return null;
        }
        RolePo rolePo = convert(roleDto, RolePo::new);
        rolePo.setTenantId(getTenantId());
        return rolePo;
    }

    public static Role toRole(RolePo rolePo, List<Resource> resources) {
        Role role = convert(rolePo, Role::new);
        role.setResources(resources);
        return role;
    }
}
