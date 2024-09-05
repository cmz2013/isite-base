package org.isite.tenant.service;

import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.DataApi;
import org.isite.tenant.data.vo.Rbac;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Tenant;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RolePo;
import org.isite.tenant.po.TenantPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.List.of;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.TreeConverter.toTree;
import static org.isite.commons.lang.enums.SwitchStatus.DISABLED;
import static org.isite.tenant.converter.RoleConverter.toRole;
import static org.isite.tenant.data.enums.OfficeStatus.DIMISSION;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class RbacService {

    private TenantService tenantService;
    private EmployeeService employeeService;
    private EmployeeRoleService employeeRoleService;
    private ResourceService resourceService;
    private RoleResourceService roleResourceService;
    private ResourceApiService resourceApiService;

    public Rbac getRbac(LoginDto login) {
        TenantPo tenantPo = null;
        if (null != login.getTenantId()) {
            tenantPo = tenantService.get(login.getTenantId());
        }
        List<TenantPo> tenantPos = null != tenantPo ?
                of(tenantPo) : tenantService.findByUserId(login.getUserId());

        if (isEmpty(tenantPos)) {
            return null;
        }
        for (TenantPo po : tenantPos) {
            Rbac rbac = getRbac(po, login.getUserId(), login.getClientId());
            if (null != rbac) {
                return rbac;
            }
        }
        return null;
    }

    private Rbac getRbac(TenantPo tenantPo, long userId, String clientId) {
        if (DISABLED.equals(tenantPo.getStatus()) ||
                tenantPo.getExpireTime().getTime() <= currentTimeMillis()) {
            return null;
        }
        EmployeePo employeePo = employeeService.getEmployee(tenantPo.getId(), userId);
        if (null == employeePo || DIMISSION.equals(employeePo.getOfficeStatus())) {
            return null;
        }
        Rbac rbac = new Rbac();
        rbac.setTenant(convert(tenantPo, Tenant::new));
        rbac.setEmployeeId(employeePo.getId());
        List<RolePo> rolePos = employeeRoleService.findEmployeeRoles(employeePo.getId());
        if (isEmpty(rolePos)) {
            return rbac;
        }
        rbac.setRoles(new ArrayList<>(rolePos.size()));

        //按角色分别设置系统资源（功能权限）
        rolePos.forEach(rolePo -> {
            List<ResourcePo> resourcePos = roleResourceService.findRoleResources(clientId, rolePo.getId());
            if (isEmpty(resourcePos)) {
                return;
            }
            List<Resource> resources = toTree(resourcePos, po -> convert(po, Resource::new),
                    ids -> resourceService.findIn(ResourcePo::getId, ids));
            rbac.getRoles().add(toRole(rolePo, resources));
            List<DataApi> dataApis = convert(resourceApiService
                    .findDataApis(convert(resourcePos, ResourcePo::getId)), DataApi::new);
            if (null == rbac.getDataApis()) {
                rbac.setDataApis(new HashSet<>(dataApis));
            } else {
                rbac.getDataApis().addAll(dataApis);
            }
        });
        return rbac;
    }

    @Autowired
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setEmployeeRoleService(EmployeeRoleService employeeRoleService) {
        this.employeeRoleService = employeeRoleService;
    }

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    @Autowired
    public void setResourceApiService(ResourceApiService resourceApiService) {
        this.resourceApiService = resourceApiService;
    }
}
