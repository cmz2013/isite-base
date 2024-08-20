package org.isite.security.login;

import org.isite.security.data.enums.ClientIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 租户登录处理器
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class TenantLoginHandler implements LoginHandler {

    private BCryptMatcher passwordMatcher;
    private OauthEmployeeService oauthEmployeeService;

    @Override
    public UserDetailsService getUserDetailsService() {
        return oauthEmployeeService;
    }

    @Override
    public PasswordMatcher getPasswordMatcher() {
        return passwordMatcher;
    }

    @Autowired
    public void setPasswordEncoder(BCryptMatcher passwordMatcher) {
        this.passwordMatcher = passwordMatcher;
    }

    @Autowired
    public void setOauthEmployeeService(OauthEmployeeService oauthEmployeeService) {
        this.oauthEmployeeService = oauthEmployeeService;
    }

    @Override
    public ClientIdentifier[] getIdentities() {
        //不会注册到LoginHandlerFactory工厂类中
        return null;
    }
}
