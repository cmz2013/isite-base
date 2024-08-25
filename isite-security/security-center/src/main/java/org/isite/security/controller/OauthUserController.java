package org.isite.security.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.data.oauth.OauthUser;
import org.isite.security.oauth.TenantService;
import org.isite.security.oauth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

import static org.isite.security.converter.EmployeeConverter.toEmployeeMap;
import static org.isite.security.data.constants.UrlConstants.DELETE_OAUTH_USER;
import static org.isite.security.data.constants.UrlConstants.GET_OAUTH_PRINCIPAL;
import static org.isite.security.data.constants.UrlConstants.GET_OAUTH_USER;
import static org.isite.security.data.constants.UrlConstants.PUT_OAUTH_TENANT;
import static org.isite.security.web.utils.SecurityUtils.getOauthEmployee;
import static org.isite.security.web.utils.SecurityUtils.getOauthUser;
import static org.isite.security.web.utils.SecurityUtils.getTokenValue;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class OauthUserController extends BaseController {

    private TokenService tokenService;

    private TenantService tenantService;

    /**
     * 给资源服务器提供Token认证的API，Spring Security注入参数
     */
    @GetMapping(GET_OAUTH_PRINCIPAL)
    public Principal getPrincipal(Principal principal) {
        return principal;
    }

    /**
     * 获取当前登录的用户信息
     */
    @GetMapping(GET_OAUTH_USER)
    public Result<OauthUser> getUser() {
        return toResult(getOauthUser());
    }

    @DeleteMapping(DELETE_OAUTH_USER)
    public Result<Boolean> revokeToken() {
        return toResult(tokenService.revokeToken(getTokenValue()));
    }

    /**
     * 多租户的用户，登录以后可以切换租户，同时更新rbac权限信息
     */
    @PutMapping(PUT_OAUTH_TENANT)
    public Result<Map<String, Object>> switchTenant(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam(value = "hasRole", required = false, defaultValue = "false") Boolean hasRole) {
        return toResult(toEmployeeMap(tenantService.changeTenant(getOauthEmployee(), tenantId), hasRole));
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }
}
