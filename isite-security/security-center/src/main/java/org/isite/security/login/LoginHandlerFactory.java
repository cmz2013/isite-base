package org.isite.security.login;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.security.config.ClientConfig;
import org.isite.security.data.enums.ClientIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.security.constants.SecurityConstants.AUTHORIZATION_SCOPE_RBAC;

/**
 * @Description 登录接口工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class LoginHandlerFactory extends AbstractFactory<LoginHandler, ClientIdentifier, String> {

    private UserLoginHandler userLoginHandler;
    private TenantLoginHandler tenantLoginHandler;
    private ClientConfig clientConfig;

    /**
     * 如果没有自定义客户端（clientId）配置，系统根据授权方式默认使用: TenantLoginEndpoint / UserLoginEndpoint
     */
    public LoginHandler getLoginHandler(String clientId) {
        LoginHandler loginHandler = this.get(clientId);
        if (null == loginHandler) {
            return clientConfig.getClientProperties(clientId).getScopes()
                    .contains(AUTHORIZATION_SCOPE_RBAC) ? tenantLoginHandler : userLoginHandler;
        }
        return loginHandler;
    }

    @Autowired
    public void setUserLoginHandler(UserLoginHandler userLoginHandler) {
        this.userLoginHandler = userLoginHandler;
    }

    @Autowired
    public void setTenantLoginHandler(TenantLoginHandler tenantLoginHandler) {
        this.tenantLoginHandler = tenantLoginHandler;
    }

    @Autowired
    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
}
