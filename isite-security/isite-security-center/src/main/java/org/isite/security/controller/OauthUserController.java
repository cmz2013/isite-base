package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.data.constants.SecurityUrls;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.oauth.TokenService;
import org.isite.security.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class OauthUserController extends BaseController {
    private TokenService tokenService;

    /**
     * 给资源服务器提供Token认证的API
     */
    @GetMapping(SecurityUrls.GET_OAUTH_PRINCIPAL)
    public OauthUser getPrincipal() {
        return SecurityUtils.getOauthUser();
    }

    /**
     * 获取当前登录的用户信息
     */
    @GetMapping(SecurityUrls.GET_OAUTH_USER)
    public Result<OauthUser> getUser() {
        return toResult(SecurityUtils.getOauthUser());
    }

    @DeleteMapping(SecurityUrls.DELETE_OAUTH_USER)
    public Result<Boolean> revokeToken() {
        return toResult(tokenService.revokeToken(SecurityUtils.getTokenValue()));
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
