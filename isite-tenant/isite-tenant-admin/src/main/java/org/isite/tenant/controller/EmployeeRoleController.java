package org.isite.tenant.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.tenant.converter.EmployeeRoleConverter;
import org.isite.tenant.data.constants.TenantUrls;
import org.isite.tenant.po.EmployeeRolePo;
import org.isite.tenant.service.EmployeeRoleService;
import org.isite.tenant.service.EmployeeService;
import org.isite.tenant.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @Description 给员工设置角色。
 */
@RestController
public class EmployeeRoleController extends BaseController {
    private EmployeeService employeeService;
    private RoleService roleService;
    private EmployeeRoleService employeeRoleService;

    /**
     * 给员工设置角色，覆盖原有角色。
     */
    @Validated
    @PostMapping(TenantUrls.URL_TENANT + "/employee/{employeeId}/roles")
    public Result<Integer> addEmployeeRoles(@PathVariable("employeeId") long employeeId,
                                            @Size(min = Constants.ONE, max = Constants.THOUSAND) @RequestBody List<Integer> roleIds) {
        int tenantId = TransmittableHeaders.getTenantId();
        Assert.isTrue(employeeService.get(employeeId).getTenantId().equals(tenantId), new OverstepAccessError());
        int adminRoleId = roleService.getAdminRole(tenantId).getId();
        Assert.isTrue(roleIds.contains(adminRoleId) || employeeRoleService.hasAdminRole(tenantId, adminRoleId, employeeId),
                MessageUtils.getMessage("employee.admin.none", "employees with the administrator role, at least one"));
        return toResult(employeeRoleService.deleteAndInsert(EmployeeRolePo::getEmployeeId, employeeId,
                roleIds.stream().map(roleId -> new EmployeeRolePo(employeeId, roleId, tenantId)).collect(Collectors.toList())));
    }

    /**
     * 删除员工角色，Administrator 角色的员工，至少要有一个，不能都删除
     */
    @DeleteMapping(TenantUrls.URL_TENANT + "/employee/{employeeId}/role/{roleId}")
    public Result<Integer> deleteEmployeeRoles(
            @PathVariable("employeeId") long employeeId, @PathVariable("roleId") int roleId) {
        int tenantId = TransmittableHeaders.getTenantId();
        Assert.isTrue(employeeService.get(employeeId).getTenantId().equals(tenantId), new OverstepAccessError());
        int adminRoleId = roleService.getAdminRole(tenantId).getId();
        Assert.isTrue(roleId != adminRoleId || employeeRoleService.hasAdminRole(tenantId, adminRoleId, employeeId),
                MessageUtils.getMessage("employee.admin.none", "employees with the administrator role, at least one"));
        return toResult(employeeRoleService.delete(EmployeeRoleConverter.toEmployeeRoleSelectivePo(employeeId, roleId)));
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setEmployeeRoleService(EmployeeRoleService employeeRoleService) {
        this.employeeRoleService = employeeRoleService;
    }
}
