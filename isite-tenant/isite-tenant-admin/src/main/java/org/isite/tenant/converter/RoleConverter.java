package org.isite.tenant.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.tenant.data.dto.RoleDto;
import org.isite.tenant.data.dto.RoleGetDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.po.RolePo;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class RoleConverter {

    private RoleConverter() {
    }

    public static RolePo toRolePo(RoleDto roleDto) {
        RolePo rolePo = DataConverter.convert(roleDto, RolePo::new);
        rolePo.setTenantId(TransmittableHeaders.getTenantId());
        if (null == rolePo.getRemark()) {
            rolePo.setRemark(Constants.BLANK_STR);
        }
        return rolePo;
    }

    public static RolePo toRoleSelectivePo(RoleDto roleDto) {
        RolePo rolePo = DataConverter.convert(roleDto, RolePo::new);
        if (null == rolePo.getRemark()) {
            rolePo.setRemark(Constants.BLANK_STR);
        }
        return rolePo;
    }

    public static RolePo toRoleSelectivePo(int tenantId, RoleGetDto roleGetDto) {
        RolePo rolePo = DataConverter.convert(roleGetDto, RolePo::new);
        rolePo.setTenantId(tenantId);
        return rolePo;
    }

    public static RolePo toRoleSelectivePo(int tenantId, String roleName) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setRoleName(roleName);
        return rolePo;
    }

    public static Role toRole(RolePo rolePo, List<Resource> resources) {
        Role role = DataConverter.convert(rolePo, Role::new);
        role.setResources(resources);
        return role;
    }
}
