package org.isite.tenant.service;

import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.RoleMapper;
import org.isite.tenant.po.RolePo;
import org.isite.tenant.po.RoleResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.tenant.converter.RoleResourceConverter.toRoleResourcePos;
import static org.isite.tenant.data.constant.CacheKey.LOCK_TENANT;
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
     * @Description 运营管理员新增租户时，系统自动给该租户添加 Administrator 角色。
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
     * @Description 运营管理员修改租户时，系统自动修改租户的 Administrator 角色权限。
     * 运营管理员修改租户Administrator 角色权限时，租户不能新增或修改角色权限。
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = LOCK_TENANT, keys = "#tenantId"))
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

    /**
     * 一个租户有且只有一个 Administrator 角色
     */
    public RolePo getAdminRole(int tenantId) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(ROLE_ADMINISTRATOR);
        return this.findOne(rolePo);
    }

    /**
     * @Description 租户自己新增角色
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = LOCK_TENANT, keys = "#tenantId"))
    public int addRole(RolePo rolePo, List<Integer> resourceIds) {
        checkResources(rolePo.getTenantId(), resourceIds);
        this.insert(rolePo);
        roleResourceService.insert(toRoleResourcePos(rolePo.getId(), resourceIds));
        return rolePo.getId();
    }

    /**
     * 检查是否超出管理员权限范围。必须在同步锁内完成校验，保证数据一致性
     */
    private void checkResources(int tenantId, List<Integer> resourceIds) {
        int adminRoleId = getAdminRole(tenantId).getId();
        Set<Integer> adminResourceIds = roleResourceService.findList(RoleResourcePo::getRoleId, adminRoleId)
                .stream().map(RoleResourcePo::getResourceId).collect(toSet());
        isTrue(adminResourceIds.containsAll(resourceIds), new OverstepAccessError());
    }

    /**
     * @Description 租户自己修改角色，先删除原有权限，再添加新权限
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = LOCK_TENANT, keys = "#tenantId"))
    public int updateRole(RolePo rolePo, List<Integer> resourceIds) {
        checkResources(rolePo.getTenantId(), resourceIds);
        return roleResourceService.deleteAndInsert(RoleResourcePo::getRoleId,rolePo.getId(),
                toRoleResourcePos(rolePo.getId(), resourceIds));
    }

    public boolean exists(int tenantId, String name) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(name);
        return exists(rolePo);
    }

    public boolean exists(int tenantId, String name, int excludeId) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(name);
        return exists(rolePo, excludeId);
    }
}
