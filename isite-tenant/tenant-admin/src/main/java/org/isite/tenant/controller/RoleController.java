package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.tenant.data.dto.RoleDto;
import org.isite.tenant.data.dto.RoleGetDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RolePo;
import org.isite.tenant.service.ResourceService;
import org.isite.tenant.service.RoleResourceService;
import org.isite.tenant.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.commons.cloud.data.TreeConverter.toTree;
import static org.isite.tenant.converter.RoleConverter.toRole;
import static org.isite.tenant.converter.RoleConverter.toRolePo;
import static org.isite.tenant.converter.RoleConverter.toRoleQuery;
import static org.isite.tenant.converter.RoleConverter.toRoleSelectivePo;
import static org.isite.tenant.data.constants.UrlConstants.URL_TENANT;

/**
 * @Description 角色信息 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class RoleController extends BaseController {

    private ResourceService resourceService;
    private RoleService roleService;
    private RoleResourceService roleResourceService;

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    /**
     * 租户查询角色列表
     */
    @GetMapping(URL_TENANT + "/roles")
    public PageResult<Role> findPage(PageRequest<RoleGetDto> request) {
        try (Page<RolePo> page = roleService.findPage(toRoleQuery(request))) {
            return toPageResult(request, convert(page.getResult(), Role::new), page.getTotal());
        }
    }

    /**
     * 租户查询角色详情
     */
    @GetMapping(URL_TENANT + "/role/{id}/client/{clientId}")
    public Result<Role> findById(@PathVariable("id") Integer id,
                                 @PathVariable("clientId") String clientId) {
        RolePo rolePo = roleService.get(id);
        isTrue(rolePo.getTenantId().equals(getTenantId()), new OverstepAccessError());
        List<ResourcePo> resourcePos = roleResourceService.findRoleResources(clientId, rolePo.getId());
        List<Resource> resources = toTree(resourcePos, po -> convert(po, Resource::new),
                ids -> resourceService.findIn(ResourcePo::getId, ids));
        return toResult(toRole(rolePo, resources));
    }

    /**
     * 租户的角色只能租户自己添加，系统内置超管不能操作用户数据.
     * SpringMVC框架提交参数list时，默认只能接收到256个数据，可以单独Controller或者全局进行配置，
     * 否则当前端页面传的数组数据长度大于256位的时候就会报错
     */
    @PostMapping(URL_TENANT + "/role")
    public Result<Integer> addRole(@RequestBody @Validated(Add.class) RoleDto roleDto) {
        isFalse(roleService.exists(toRoleSelectivePo(getTenantId(), roleDto.getRoleName())),
                getMessage("role.exists", "Role name already exists"));
        return toResult(roleService.addRole(toRolePo(roleDto), roleDto.getResourceIds()));
    }

    /**
     * 租户的角色只能租户自己修改，系统内置超管不能操作用户数据
     */
    @PutMapping(URL_TENANT + "/role")
    public Result<Integer> updateRole(@RequestBody @Validated(Update.class) RoleDto roleDto) {
        int tenantId = getTenantId();
        isFalse(roleService.getAdminRole(tenantId).getId().equals(roleDto.getId()),
                "You cannot modify the Administrator role");
        isTrue(roleService.get(roleDto.getId()).getTenantId().equals(tenantId), new OverstepAccessError());
        isFalse(roleService.exists(toRoleSelectivePo(getTenantId(), roleDto.getRoleName()), roleDto.getId()),
                getMessage("role.exists", "Role name already exists"));
        return toResult(roleService.updateRole(toRoleSelectivePo(roleDto), roleDto.getResourceIds()));
    }

    @DeleteMapping(URL_TENANT + "/role/{id}")
    public Result<Integer> deleteRole(@PathVariable("id") Integer id) {
        int tenantId = getTenantId();
        isFalse(roleService.getAdminRole(tenantId).getId().equals(id), "You cannot delete the Administrator role");
        isTrue(roleService.get(id).getTenantId().equals(tenantId), new OverstepAccessError());
        return toResult(roleService.delete(id));
    }
}
