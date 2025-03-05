package org.isite.security.controller;

import org.isite.security.constants.SecurityConstants;
import org.isite.security.data.constants.SecurityUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
/**
 * @Description 通过@SessionAttributes注解将ModelMap中的属性存入到Session中
 * @Author <font color='blue'>zhangcm</font>
 */
@Controller
@SessionAttributes(SecurityConstants.AUTHORIZATION_REQUEST)
public class AccessConfirmController {
    private UserLoginController userLoginController;

    /**
     * 授权码模式，用户登录以后批准授权
     */
    @RequestMapping(value = SecurityUrls.URL_OAUTH_APPROVAL, method = {RequestMethod.POST, RequestMethod.GET})
    public String getApproval(Model model) {
        if (null == model.getAttribute(SecurityConstants.AUTHORIZATION_REQUEST)){
            return userLoginController.getLoginForm(null, model);
        }
        return "approval";
    }

    @Autowired
    public void setLoginController(UserLoginController userLoginController) {
        this.userLoginController = userLoginController;
    }
}