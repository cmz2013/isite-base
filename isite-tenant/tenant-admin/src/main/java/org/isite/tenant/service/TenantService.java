package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.data.dto.TenantDto;
import org.isite.tenant.mapper.TenantMapper;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.po.EmployeeRolePo;
import org.isite.tenant.po.RolePo;
import org.isite.tenant.po.RoleResourcePo;
import org.isite.tenant.po.TenantPo;
import org.isite.user.data.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.tenant.converter.EmployeeConverter.toEmployeePo;
import static org.isite.tenant.converter.RoleResourceConverter.toRoleResourcePos;
import static org.isite.tenant.converter.TenantConverter.toTenantPo;
import static org.isite.tenant.converter.TenantConverter.toTenantSelectivePo;
import static org.isite.user.client.UserAccessor.getUser;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TenantService extends PoService<TenantPo, Integer> {

    private RoleService roleService;
    private RoleResourceService roleResourceService;
    private EmployeeService employeeService;
    private EmployeeRoleService employeeRoleService;

    @Autowired
    public TenantService(TenantMapper tenantMapper) {
        super(tenantMapper);
    }

    public List<TenantPo> findByUserId(long userId) {
        return ((TenantMapper) getMapper()).selectByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTenant(TenantDto tenantDto) {
        //查询已注册的用户信息
        User user = getUser(tenantDto.getPhone());
        notNull(user, getMessage("user.notRegister",
                "the user is not registered: " + tenantDto.getPhone()));
        TenantPo tenantPo = toTenantPo(tenantDto);
        this.insert(tenantPo);
        int roleId = roleService.addAdminRole(tenantPo.getId());
        roleResourceService.insert(toRoleResourcePos(roleId, tenantDto.getResourceIds()));
        EmployeePo employeePo = toEmployeePo(user.getId(), tenantPo.getId());
        employeeService.insert(employeePo);
        employeeRoleService.insert(new EmployeeRolePo(employeePo.getId(), roleId));
    }

    /**
     * 1）编辑租户信息时，只修改Administrator角色权限，不修改管理员员工信息.
     * 2）不会立即踢出该租户的员工登录，所以，不会影响到员工的正常使用，下次登录更新权限即可.
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTenant(TenantDto tenantDto) {
        this.updateSelectiveById(toTenantSelectivePo(tenantDto));

        RolePo adminRole = roleService.getAdminRole(tenantDto.getId());
        List<Integer> oldResourceIds = roleResourceService.findResourceIds(adminRole.getId());
        //回收资源
        roleResourceService.deleteRoleResources(tenantDto.getId(), new LinkedList<>(oldResourceIds).stream()
                .filter(resourceId -> !tenantDto.getResourceIds().contains(resourceId)).collect(toList()));
        //administrator角色添加新增的资源
        roleResourceService.insert(tenantDto.getResourceIds().stream().filter(
                resourceId -> !oldResourceIds.contains(resourceId)).map(
                resourceId -> new RoleResourcePo(adminRole.getId(), resourceId)).collect(toList()));
    }


    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setEmployeeRoleService(EmployeeRoleService employeeRoleService) {
        this.employeeRoleService = employeeRoleService;
    }
}
