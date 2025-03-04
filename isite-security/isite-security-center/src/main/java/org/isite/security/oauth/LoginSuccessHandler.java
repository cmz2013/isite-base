package org.isite.security.oauth;

import org.isite.security.data.constants.SecurityUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
/**
 * @Description 定义授权码模式下，用户登录成功之后的操作
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private WebRedirectStrategy redirectStrategy;

    @Autowired
    public void setRedirectStrategy(WebRedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @PostConstruct
    private void init() {
        super.setDefaultTargetUrl(SecurityUrls.URL_LOGIN_FORM + "?logout");
        super.setRedirectStrategy(redirectStrategy);
    }
}
