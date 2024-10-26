package org.isite.tenant.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.sign.Signed;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.isite.tenant.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.tenant.data.constants.UrlConstants.API_GET_EMPLOYEE_RBAC;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class RbacController extends BaseController {

    private RbacService rbacService;

    @Signed
    @GetMapping(API_GET_EMPLOYEE_RBAC)
    public Result<Rbac> getRbac(@Validated LoginDto loginDto) {
        return toResult(rbacService.getRbac(loginDto));
    }

    @Autowired
    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }
}
