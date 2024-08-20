package org.isite.security.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.data.dto.UserPostDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.oauth.OauthEmployee;
import org.isite.security.data.oauth.OauthUser;
import org.isite.security.oauth.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static org.isite.security.converter.EmployeeConverter.toEmployeeMap;
import static org.isite.security.converter.UserConverter.toUserMap;
import static org.isite.security.data.constants.UrlConstants.API_POST_USER;
import static org.isite.security.data.constants.UrlConstants.API_PUT_USER_PASSWORD;
import static org.isite.security.data.constants.UrlConstants.POST_LOGIN_PROCESS;
import static org.isite.security.data.constants.UrlConstants.URL_LOGIN_FORM;
import static org.isite.security.data.constants.UrlConstants.URL_OAUTH;
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
    public Result<Integer> updatePassword(@RequestBody @Validated UserSecretDto secret) {
        return toResult(userLoginService.updatePassword(secret));
    }

    /**
     * 校验验证码，注册用户信息
     */
    @PostMapping(API_POST_USER)
    public Result<Integer> addUser(@Validated @RequestBody UserPostDto userPostDto) {
        return toResult(userLoginService.addUser(userPostDto));
    }

    /**
     * 获取当前登录用户的功能授权
     */
    @GetMapping(URL_OAUTH + "/resources")
    public Result<Map<String, Object>> getUserResources(
            @RequestParam(value = "hasRole", required = false, defaultValue = "false") Boolean hasRole) {
        OauthUser oauthUser = getOauthUser();
        return toResult(oauthUser instanceof OauthEmployee ?
                toEmployeeMap((OauthEmployee) oauthUser, hasRole) : toUserMap(oauthUser));
    }

    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }
}
