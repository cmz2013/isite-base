package org.isite.security.login;

import org.isite.security.data.enums.ClientIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 用户中心登录处理器
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserLoginHandler implements LoginHandler {

    private OauthUserService oauthUserService;
    private BCryptMatcher passwordMatcher;

    @Override
    public UserDetailsService getUserDetailsService() {
        return oauthUserService;
    }

    @Override
    public BCryptMatcher getPasswordMatcher() {
        return passwordMatcher;
    }

    @Autowired
    public void setPasswordMatcher(BCryptMatcher passwordMatcher) {
        this.passwordMatcher = passwordMatcher;
    }

    @Autowired
    public void setOauthUserService(OauthUserService oauthUserService) {
        this.oauthUserService = oauthUserService;
    }

    @Override
    public ClientIdentifier[] getIdentities() {
        //不会注册到LoginHandlerFactory工厂类中
        return null;
    }
}
