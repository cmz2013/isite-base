package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.oauth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.security.data.constants.SecurityUrls.DELETE_OAUTH_USER;
import static org.isite.security.data.constants.SecurityUrls.GET_OAUTH_PRINCIPAL;
import static org.isite.security.data.constants.SecurityUrls.GET_OAUTH_USER;
import static org.isite.security.web.utils.SecurityUtils.getOauthUser;
import static org.isite.security.web.utils.SecurityUtils.getTokenValue;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class OauthUserController extends BaseController {

    private TokenService tokenService;

    /**
     * 给资源服务器提供Token认证的API
     */
    @GetMapping(GET_OAUTH_PRINCIPAL)
    public OauthUser getPrincipal() {
        return getOauthUser();
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

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
