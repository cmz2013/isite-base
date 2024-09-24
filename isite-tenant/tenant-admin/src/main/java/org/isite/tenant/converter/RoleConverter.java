package org.isite.tenant.converter;

import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.jpa.data.PageQuery;
import org.isite.tenant.data.dto.RoleDto;
import org.isite.tenant.data.dto.RoleGetDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.po.RolePo;

import java.util.List;

import static org.isite.commons.cloud.converter.Converter.convert;
import static org.isite.commons.cloud.converter.Converter.toPageQuery;
import static org.isite.commons.lang.Constants.BLANK_STRING;
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
            rolePo.setRemark(BLANK_STRING);
        }
        return rolePo;
    }

    public static RolePo toRoleSelectivePo(RoleDto roleDto) {
        RolePo rolePo = convert(roleDto, RolePo::new);
        if (null == rolePo.getRemark()) {
            rolePo.setRemark(BLANK_STRING);
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

    public static PageQuery<RolePo> toRoleQuery(PageRequest<RoleGetDto> request) {
        PageQuery<RolePo> pageQuery = toPageQuery(request, RolePo::new);
        if (null == pageQuery.getPo()) {
            pageQuery.setPo(new RolePo());
        }
        pageQuery.getPo().setTenantId(getTenantId());
        return pageQuery;
    }
}
