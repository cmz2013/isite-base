package org.isite.tenant.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.sign.Signature;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.isite.tenant.po.RoleResourcePo;
import org.isite.tenant.service.RbacService;
import org.isite.tenant.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;

import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.tenant.converter.RoleResourceConverter.toRoleResourcePos;
import static org.isite.tenant.data.constant.UrlConstants.API_GET_EMPLOYEE_RBAC;
import static org.isite.tenant.data.constant.UrlConstants.URL_TENANT;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class RbacController extends BaseController {

    private RbacService rbacService;
    private RoleResourceService roleResourceService;

    @Signature
    @GetMapping(API_GET_EMPLOYEE_RBAC)
    public Result<Rbac> getRbac(@Validated LoginDto loginDto) {
        return toResult(rbacService.getRbac(loginDto));
    }

    /**
     * @Description SpringMVC框架提交参数list时，默认只能接收到256个数据，可以单独Controller或者全局进行配置
     * 否则，当你前端页面传的数组数据长度大于256位的时候就会报错
     */
    @PutMapping(URL_TENANT + "/role/{roleId}/resources")
    public Result<Integer> resetResources(
            @PathVariable("roleId") Integer roleId,
            @Validated @RequestBody @Size(min = ONE, max = THOUSAND) List<Integer> resourceIds) {
        return toResult(roleResourceService.deleteAndInsert(
                RoleResourcePo::getRoleId, roleId, toRoleResourcePos(roleId, resourceIds)));
    }

    @Autowired
    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }
}
