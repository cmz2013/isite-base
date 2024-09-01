package org.isite.tenant.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
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

import static java.util.stream.Collectors.toList;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.EmployeeRoleConverter.toEmployeeRoleSelectivePo;
import static org.isite.tenant.data.constants.UrlConstants.URL_TENANT;

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
    @PostMapping(URL_TENANT + "/employee/{employeeId}/roles")
    public Result<Integer> addEmployeeRoles(@PathVariable("employeeId") long employeeId,
                                            @Size(min = ONE, max = THOUSAND) @RequestBody List<Integer> roleIds) {
        int tenantId = getTenantId();
        isTrue(employeeService.get(employeeId).getTenantId().equals(tenantId), new OverstepAccessError());
        int adminRoleId = roleService.getAdminRole(tenantId).getId();
        isTrue(roleIds.contains(adminRoleId) || employeeRoleService.hasAdminRole(tenantId, adminRoleId, employeeId),
                getMessage("employee.admin.none", "Employees with the Administrator role, at least one"));
        return toResult(employeeRoleService.deleteAndInsert(EmployeeRolePo::getEmployeeId, employeeId,
                roleIds.stream().map(roleId -> new EmployeeRolePo(employeeId, roleId, tenantId)).collect(toList())));
    }

    /**
     * 删除员工角色，Administrator 角色的员工，至少要有一个，不能都删除
     */
    @DeleteMapping(URL_TENANT + "/employee/{employeeId}/role/{roleId}")
    public Result<Integer> deleteEmployeeRoles(
            @PathVariable("employeeId") long employeeId, @PathVariable("roleId") int roleId) {
        int tenantId = getTenantId();
        isTrue(employeeService.get(employeeId).getTenantId().equals(tenantId), new OverstepAccessError());
        int adminRoleId = roleService.getAdminRole(tenantId).getId();
        isTrue(roleId != adminRoleId || employeeRoleService.hasAdminRole(tenantId, adminRoleId, employeeId),
                getMessage("employee.admin.none", "Employees with the Administrator role, at least one"));
        return toResult(employeeRoleService.delete(toEmployeeRoleSelectivePo(employeeId, roleId)));
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
