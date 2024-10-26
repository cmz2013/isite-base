package org.isite.security.login;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.security.data.enums.ClientIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 登录接口工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ClientLoginFactory extends AbstractFactory<ClientLogin, ClientIdentifier, String> {

    private UserClientLogin userClientLogin;

    /**
     * 如果没有自定义客户端（clientId）配置，系统根据授权方式默认使用: TenantLoginEndpoint / UserLoginEndpoint
     */
    @Override
    public ClientLogin get(String clientId) {
        ClientLogin clientLogin = super.get(clientId);
        return null == clientLogin ? userClientLogin : clientLogin;
    }

    @Autowired
    public void setUserClientLogin(UserClientLogin userClientLogin) {
        this.userClientLogin = userClientLogin;
    }
}
