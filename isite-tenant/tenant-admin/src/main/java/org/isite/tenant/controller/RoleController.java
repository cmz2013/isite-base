package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
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
import static org.isite.tenant.converter.RoleConverter.toRolePo;
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
    public Result<Integer> addRole(@RequestBody @Validated(Add.class) RoleDto role) {
        return toResult(roleService.insert(toRolePo(role)));
    }

    @PutMapping(URL_TENANT + "/role")
    public Result<Integer> updateRole(@RequestBody @Validated(Update.class) RoleDto role) {
        return toResult(roleService.updateById(toRolePo(role)));
    }

    @DeleteMapping(URL_TENANT + "/role/{id}")
    public Result<Integer> deleteRole(@PathVariable("id") Integer id){
        return toResult(roleService.delete(id));
    }
}
