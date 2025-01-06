package org.isite.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.isite.security.data.constants.UrlConstants.URL_LOGIN_FORM;

/**
 * @Description 授权码模式下，用户退出登录之后的操作
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private WebRedirectStrategy redirectStrategy;

    @PostConstruct
    private void init() {
        super.setDefaultTargetUrl(URL_LOGIN_FORM + "?logout");
        super.setRedirectStrategy(redirectStrategy);
    }

    @Autowired
    public void setRedirectStrategy(WebRedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}