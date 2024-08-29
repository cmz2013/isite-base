package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.RoleMapper;
import org.isite.tenant.po.RolePo;
import org.isite.tenant.po.RoleResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.tenant.converter.RoleResourceConverter.toRoleResourcePos;
import static org.isite.tenant.data.constant.TenantConstants.ROLE_ADMINISTRATOR;

/**
 * @Description 角色信息Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class RoleService extends PoService<RolePo, Integer> {

    private RoleResourceService roleResourceService;

    @Autowired
    public RoleService(RoleMapper roleMapper) {
        super(roleMapper);
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    /**
     * @Description 运营管理员创建租户时，给该租户添加 Administrator 角色。
     * 租户不可以自己修改或删除 Administrator 角色。
     */
    @Transactional(rollbackFor = Exception.class)
    public int addAdminRole(int tenantId, List<Integer> resourceIds) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(ROLE_ADMINISTRATOR);
        rolePo.setRemark(BLANK_STRING);
        this.insert(rolePo);
        int roleId = rolePo.getId();
        roleResourceService.insert(toRoleResourcePos(roleId, resourceIds));
        return roleId;
    }

    /**
     * 一个租户有且只有一个 Administrator 角色
     */
    private RolePo getAdminRole(int tenantId) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(ROLE_ADMINISTRATOR);
        return this.findOne(rolePo);
    }

    /**
     * 运营管理员修改租户的 Administrator 角色权限
     */
    public void updateAdminRole(int tenantId, List<Integer> resourceIds) {
        RolePo adminRole = getAdminRole(tenantId);
        List<Integer> oldResourceIds = roleResourceService.findResourceIds(adminRole.getId());
        //回收资源
        roleResourceService.deleteRoleResources(tenantId, new LinkedList<>(oldResourceIds).stream()
                .filter(resourceId -> !resourceIds.contains(resourceId)).collect(toList()));
        //administrator角色添加新增的资源
        roleResourceService.insert(resourceIds.stream().filter(
                resourceId -> !oldResourceIds.contains(resourceId)).map(
                resourceId -> new RoleResourcePo(adminRole.getId(), resourceId)).collect(toList()));
    }
}
