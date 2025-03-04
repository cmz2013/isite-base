package org.isite.tenant.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.TreeConverter;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.tenant.converter.RoleConverter;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.enums.OfficeStatus;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
                Collections.singletonList(tenantPo) : tenantService.findByUserId(login.getUserId());
        if (CollectionUtils.isEmpty(tenantPos)) {
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
        if (ActiveStatus.DISABLED.equals(tenantPo.getStatus()) ||
                tenantPo.getExpireTime().isBefore(LocalDateTime.now())) {
            return null;
        }
        EmployeePo employeePo = employeeService.getEmployee(tenantPo.getId(), userId);
        if (null == employeePo || OfficeStatus.DIMISSION.equals(employeePo.getOfficeStatus())) {
            return null;
        }
        Rbac rbac = new Rbac();
        rbac.setTenant(DataConverter.convert(tenantPo, Tenant::new));
        rbac.setEmployeeId(employeePo.getId());
        List<RolePo> rolePos = employeeRoleService.findEmployeeRoles(employeePo.getId());
        if (CollectionUtils.isEmpty(rolePos)) {
            return rbac;
        }
        rbac.setRoles(new ArrayList<>(rolePos.size()));

        //按角色分别设置系统资源（功能权限）
        rolePos.forEach(rolePo -> {
            List<ResourcePo> resourcePos = roleResourceService.findRoleResources(clientId, rolePo.getId());
            if (CollectionUtils.isEmpty(resourcePos)) {
                return;
            }
            List<Resource> resources = TreeConverter.toTree(resourcePos, po -> DataConverter.convert(po, Resource::new),
                    ids -> resourceService.findIn(ResourcePo::getId, ids));
            rbac.getRoles().add(RoleConverter.toRole(rolePo, resources));
            List<DataApi> dataApis = DataConverter.convert(
                    resourceApiService.findDataApis(DataConverter.convert(resourcePos, ResourcePo::getId)), DataApi::new);
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
