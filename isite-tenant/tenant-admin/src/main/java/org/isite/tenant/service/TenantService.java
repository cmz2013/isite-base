package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.TenantMapper;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.po.EmployeeRolePo;
import org.isite.tenant.po.TenantPo;
import org.isite.user.data.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.isite.tenant.converter.EmployeeConverter.toEmployeePo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TenantService extends PoService<TenantPo, Integer> {

    private EmployeeService employeeService;
    private RoleService roleService;
    private EmployeeRoleService employeeRoleService;

    @Autowired
    public TenantService(TenantMapper tenantMapper) {
        super(tenantMapper);
    }

    public List<TenantPo> findByUserId(long userId) {
        return ((TenantMapper) getMapper()).selectByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer addTenant(User user, TenantPo tenantPo, List<Integer> resourceIds) {
        this.insert(tenantPo);
        int roleId = roleService.addAdminRole(tenantPo.getId(), resourceIds);
        EmployeePo employeePo = toEmployeePo(user.getId(), tenantPo.getId());
        employeeService.insert(employeePo);
        employeeRoleService.insert(new EmployeeRolePo(employeePo.getId(), roleId));
        return tenantPo.getId();
    }

    /**
     * 1）编辑租户信息时，只修改Administrator角色权限，不修改管理员员工信息.
     * 2）不会立即踢出该租户的员工登录，所以，不会影响到员工的正常使用，下次登录更新权限即可.
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateTenant(TenantPo tenantPo, List<Integer> resourceIds) {
        roleService.updateAdminRole(tenantPo.getId(), resourceIds);
        return this.updateSelectiveById(tenantPo);
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
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
