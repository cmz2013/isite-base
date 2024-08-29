package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.tenant.data.dto.RoleDto;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.po.RolePo;
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

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.RoleConverter.toRolePo;
import static org.isite.tenant.converter.RoleConverter.toRoleSelectivePo;
import static org.isite.tenant.data.constant.UrlConstants.URL_TENANT;

/**
 * @Description 角色信息 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class RoleController extends BaseController {
    /**
     * 角色Service
     */
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(URL_TENANT + "/roles")
    public PageResult<Role> findPage(PageRequest<RoleDto> request) {
        try (Page<RolePo> page = roleService.findPage(toPageQuery(request, RolePo::new))) {
            return toPageResult(request, convert(page.getResult(), Role::new), page.getTotal());
        }
    }

    @GetMapping(URL_TENANT + "/role/{id}")
    public Result<Role> findById(@PathVariable("id") Integer id) {
        return toResult(convert(roleService.get(id), Role::new));
    }

    /**
     * 租户的角色只能租户自己添加，系统内置超管不能操作用户数据
     */
    @PostMapping(URL_TENANT + "/role")
    public Result<Integer> addRole(@RequestBody @Validated(Add.class) RoleDto roleDto) {
        isFalse(roleService.exists(getTenantId(), roleDto.getName()),
                getMessage("role.exists", "Role name already exists"));
        return toResult(roleService.addRole(toRolePo(roleDto), roleDto.getResourceIds()));
    }

    @PutMapping(URL_TENANT + "/role")
    public Result<Integer> updateRole(@RequestBody @Validated(Update.class) RoleDto roleDto) {
        isFalse(roleService.getAdminRole(getTenantId()).getId().equals(roleDto.getId()),
                "You cannot modify the Administrator role");
        isTrue(roleService.get(roleDto.getId()).getTenantId().equals(getTenantId()), new OverstepAccessError());
        isFalse(roleService.exists(getTenantId(), roleDto.getName(), roleDto.getId()),
                getMessage("role.exists", "Role name already exists"));
        return toResult(roleService.updateRole(toRoleSelectivePo(roleDto), roleDto.getResourceIds()));
    }

    @DeleteMapping(URL_TENANT + "/role/{id}")
    public Result<Integer> deleteRole(@PathVariable("id") Integer id) {
        isFalse(roleService.getAdminRole(getTenantId()).getId().equals(id),
                "You cannot delete the Administrator role");
        isTrue(roleService.get(id).getTenantId().equals(getTenantId()), new OverstepAccessError());
        return toResult(roleService.delete(id));
    }
}
