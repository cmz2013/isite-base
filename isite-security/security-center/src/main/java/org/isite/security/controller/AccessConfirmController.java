package org.isite.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import static org.isite.security.constants.SecurityConstants.AUTHORIZATION_REQUEST;
import static org.isite.security.data.constants.UrlConstants.URL_OAUTH_APPROVAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 通过@SessionAttributes注解将ModelMap中的属性存入到Session中
 * @author <font color='blue'>zhangcm</font>
 */
@Controller
@SessionAttributes(AUTHORIZATION_REQUEST)
public class AccessConfirmController {
    private UserLoginController userLoginController;

    /**
     * 授权码模式，用户登录以后批准授权
     */
    @RequestMapping(value = URL_OAUTH_APPROVAL, method = { POST, GET })
    public String getApproval(Model model) {
        if (null == model.getAttribute(AUTHORIZATION_REQUEST)){
            return userLoginController.getLoginForm(null, model);
        }
        return "approval";
    }

    @Autowired
    public void setLoginController(UserLoginController userLoginController) {
        this.userLoginController = userLoginController;
    }
}