package org.isite.security.oauth;

import org.isite.security.config.EndpointConfig;
import org.isite.security.config.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

/**
 * @Description 默认优先选择@Primary,不可以同时在多个ClientDetailsService的子类上设置
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class ClientService implements ClientDetailsService {

    private EndpointConfig endpointConfig;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        //客户端配置
        ClientProperties properties = endpointConfig.getClientProperties(clientId);
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(clientId);
        clientDetails.setScope(properties.getScopes());
        clientDetails.setClientSecret(properties.getSecret());
        clientDetails.setAuthorizedGrantTypes(properties.getGrantTypes());
        clientDetails.setAccessTokenValiditySeconds(properties.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(properties.getRefreshTokenValidity());
        clientDetails.setRegisteredRedirectUri(properties.getRegisteredRedirectUri());
        // 设置直接同意授权返回code，不会跳转到授权页面
//            clientDetails.setAutoApproveScopes(properties.getScope());
        return clientDetails;
    }

    @Autowired
    public void setEndpointConfig(EndpointConfig endpointConfig) {
        this.endpointConfig = endpointConfig;
    }
}
