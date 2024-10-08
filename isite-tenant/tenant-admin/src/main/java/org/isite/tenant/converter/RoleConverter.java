package org.isite.tenant.converter;

import org.isite.tenant.data.dto.RoleDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.po.RolePo;

import java.util.List;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class RoleConverter {

    private RoleConverter() {
    }

    public static RolePo toRolePo(RoleDto roleDto) {
        RolePo rolePo = convert(roleDto, RolePo::new);
        rolePo.setTenantId(getTenantId());
        if (null == rolePo.getRemark()) {
            rolePo.setRemark(BLANK_STR);
        }
        return rolePo;
    }

    public static RolePo toRoleSelectivePo(RoleDto roleDto) {
        RolePo rolePo = convert(roleDto, RolePo::new);
        if (null == rolePo.getRemark()) {
            rolePo.setRemark(BLANK_STR);
        }
        return rolePo;
    }

    public static RolePo toRoleSelectivePo(int tenantId, String roleName) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setRoleName(roleName);
        return rolePo;
    }

    public static Role toRole(RolePo rolePo, List<Resource> resources) {
        Role role = convert(rolePo, Role::new);
        role.setResources(resources);
        return role;
    }
}
