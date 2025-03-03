package org.isite.tenant.service;

import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.mybatis.service.PoService;
import org.isite.tenant.converter.RoleResourceConverter;
import org.isite.tenant.data.constants.CacheKeys;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.mapper.RoleMapper;
import org.isite.tenant.po.RolePo;
import org.isite.tenant.po.RoleResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        rolePo.setRoleName(TenantConstants.ROLE_ADMINISTRATOR);
        rolePo.setRemark(Constants.BLANK_STR);
        this.insert(rolePo);
        int roleId = rolePo.getId();
        roleResourceService.insert(RoleResourceConverter.toRoleResourcePos(roleId, resourceIds));
        return roleId;
    }

    /**
     * @Description 运营管理员修改租户时，系统自动修改租户的 Administrator 角色权限。
     * 运营管理员修改租户Administrator 角色权限时，租户不能新增或修改角色权限。
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_TENANT, keys = "#tenantId"))
    public void updateAdminRole(int tenantId, List<Integer> resourceIds) {
        RolePo adminRole = getAdminRole(tenantId);
        List<Integer> oldResourceIds = roleResourceService.findRoleResourceIds(adminRole.getId());
        //回收资源。filter方法会返回一个新的流，其中包含符合过滤条件的元素，但原始集合保持不变。
        roleResourceService.deleteRoleResources(tenantId, oldResourceIds.stream()
                .filter(resourceId -> !resourceIds.contains(resourceId)).collect(Collectors.toList()));
        //administrator角色添加新增的资源
        roleResourceService.insert(resourceIds.stream().filter(
                resourceId -> !oldResourceIds.contains(resourceId)).map(
                resourceId -> new RoleResourcePo(adminRole.getId(), resourceId)).collect(Collectors.toList()));
    }

    /**
     * 一个租户有且只有一个 Administrator 角色
     */
    public RolePo getAdminRole(int tenantId) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setRoleName(TenantConstants.ROLE_ADMINISTRATOR);
        return this.findOne(rolePo);
    }

    /**
     * @Description 租户自己新增角色
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_TENANT, keys = "#rolePo.tenantId"))
    public int addRole(RolePo rolePo, List<Integer> resourceIds) {
        checkResources(rolePo.getTenantId(), resourceIds);
        this.insert(rolePo);
        roleResourceService.insert(RoleResourceConverter.toRoleResourcePos(rolePo.getId(), resourceIds));
        return rolePo.getId();
    }

    /**
     * 检查是否超出管理员权限范围。必须在同步锁内完成校验，保证数据一致性
     */
    private void checkResources(int tenantId, List<Integer> resourceIds) {
        int adminRoleId = getAdminRole(tenantId).getId();
        Set<Integer> adminResourceIds = roleResourceService.findList(RoleResourcePo::getRoleId, adminRoleId)
                .stream().map(RoleResourcePo::getResourceId).collect(Collectors.toSet());
        Assert.isTrue(adminResourceIds.containsAll(resourceIds), new OverstepAccessError());
    }

    /**
     * @Description 租户自己修改角色，先删除原有权限，再添加新权限
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_TENANT, keys = "#rolePo.tenantId"))
    public int updateRole(RolePo rolePo, List<Integer> resourceIds) {
        checkResources(rolePo.getTenantId(), resourceIds);
        return roleResourceService.deleteAndInsert(RoleResourcePo::getRoleId, rolePo.getId(),
                RoleResourceConverter.toRoleResourcePos(rolePo.getId(), resourceIds));
    }
}
