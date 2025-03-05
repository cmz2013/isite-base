package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.converter.UserConverter;
import org.isite.security.data.constants.SecurityUrls;
import org.isite.security.data.dto.UserRegistDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.service.UserLoginService;
import org.isite.security.web.utils.SecurityUtils;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Controller
public class UserLoginController extends BaseController {
    private UserLoginService userLoginService;

    /**
     * 授权码模式用户登录页面
     */
    @RequestMapping(value = SecurityUrls.URL_LOGIN_FORM, method = {RequestMethod.GET, RequestMethod.POST})
    public String getLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("loginProcessUrl", SecurityUrls.POST_LOGIN_PROCESS);
        return "login";
    }

    /**
     * 校验验证码，更新用户密码
     */
    @ResponseBody
    @PutMapping(SecurityUrls.API_PUT_USER_PASSWORD)
    public Result<Integer> updatePassword(@RequestBody @Validated UserSecretDto userSecretDto) {
        return toResult(userLoginService.updatePassword(userSecretDto));
    }

    /**
     * 校验验证码，注册用户信息
     */
    @PostMapping(SecurityUrls.API_POST_USER)
    public Result<?> registUser(@Validated @RequestBody UserRegistDto userRegistDto) {
        return toResult(() -> userLoginService.registUser(userRegistDto));
    }

    /**
     * 获取当前登录用户的功能授权
     */
    @GetMapping(SecurityUrls.URL_OAUTH + "/resources/{clientId}")
    public Result<Map<String, Object>> getResources(
            @PathVariable("clientId") String clientId,
            @RequestParam(value = "hasRole", required = false, defaultValue = "false") Boolean hasRole) {
        OauthUser oauthUser = SecurityUtils.getOauthUser();
        List<Role> roles = userLoginService.getRoles(oauthUser, clientId);
        return toResult(UserConverter.toUserMap(oauthUser, roles, hasRole));
    }

    /**
     * 多租户的用户，登录以后可以切换租户，同时更新rbac权限信息
     */
    @PutMapping(SecurityUrls.PUT_OAUTH_TENANT)
    public Result<Map<String, Object>> changeTenant(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "hasRole", required = false, defaultValue = "false") Boolean hasRole) {
        OauthUser oauthUser = SecurityUtils.getOauthUser();
        List<Role> roles = userLoginService.changeTenant(oauthUser, tenantId, clientId);
        return toResult(UserConverter.toUserMap(oauthUser, roles, hasRole));
    }

    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }
}
