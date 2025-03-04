package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.data.dto.UserRegistDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.service.UserLoginService;
import org.isite.tenant.data.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static org.isite.security.converter.UserConverter.toUserMap;
import static org.isite.security.data.constants.SecurityUrls.API_POST_USER;
import static org.isite.security.data.constants.SecurityUrls.API_PUT_USER_PASSWORD;
import static org.isite.security.data.constants.SecurityUrls.POST_LOGIN_PROCESS;
import static org.isite.security.data.constants.SecurityUrls.PUT_OAUTH_TENANT;
import static org.isite.security.data.constants.SecurityUrls.URL_LOGIN_FORM;
import static org.isite.security.data.constants.SecurityUrls.URL_OAUTH;
import static org.isite.security.web.utils.SecurityUtils.getOauthUser;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Controller
public class UserLoginController extends BaseController {
    private UserLoginService userLoginService;

    /**
     * 授权码模式用户登录页面
     */
    @RequestMapping(value = URL_LOGIN_FORM, method = { GET, POST })
    public String getLoginForm(@RequestParam(value = "error", required = false) String error, Model model){
        model.addAttribute("error", error);
        model.addAttribute("loginProcessUrl", POST_LOGIN_PROCESS);
        return "login";
    }

    /**
     * 校验验证码，更新用户密码
     */
    @ResponseBody
    @PutMapping(API_PUT_USER_PASSWORD)
    public Result<Integer> updatePassword(@RequestBody @Validated UserSecretDto userSecretDto) {
        return toResult(userLoginService.updatePassword(userSecretDto));
    }

    /**
     * 校验验证码，注册用户信息
     */
    @PostMapping(API_POST_USER)
    public Result<?> registUser(@Validated @RequestBody UserRegistDto userRegistDto) {
        return toResult(() -> userLoginService.registUser(userRegistDto));
    }

    /**
     * 获取当前登录用户的功能授权
     */
    @GetMapping(URL_OAUTH + "/resources/{clientId}")
    public Result<Map<String, Object>> getResources(
            @PathVariable("clientId") String clientId,
            @RequestParam(value = "hasRole", required = false, defaultValue = "false") Boolean hasRole) {
        OauthUser oauthUser = getOauthUser();
        List<Role> roles = userLoginService.getRoles(oauthUser, clientId);
        return toResult(toUserMap(oauthUser, roles, hasRole));
    }

    /**
     * 多租户的用户，登录以后可以切换租户，同时更新rbac权限信息
     */
    @PutMapping(PUT_OAUTH_TENANT)
    public Result<Map<String, Object>> changeTenant(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "hasRole", required = false, defaultValue = "false") Boolean hasRole) {
        OauthUser oauthUser = getOauthUser();
        List<Role> roles = userLoginService.changeTenant(oauthUser, tenantId, clientId);
        return toResult(toUserMap(oauthUser, roles, hasRole));
    }

    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }
}
