package org.isite.security.oauth;

import org.isite.security.config.EndpointConfig;
import org.isite.security.config.ClientProperties;
import org.isite.security.login.LoginHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.lang.Assert.notNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class TokenService extends DefaultTokenServices {

    private TokenStore tokenStore;
    private TokenRenewer tokenRenewer;
    private LoginHandlerFactory loginHandlerFactory;
    private EndpointConfig endpointConfig;

    public TokenService() {
        super();
        super.setSupportRefreshToken(TRUE);
    }

    /**
     * 更新access_token时同步更新refresh_token
     * 如果属性上使用了@Value注解，该注解可直接对属性赋值，@Value的值从配置文件中获取
     */
    @Value("${security.oauth2.reuseRefreshToken}")
    public void setReuseRefreshToken(Boolean reuseRefreshToken) {
        super.setReuseRefreshToken(null == reuseRefreshToken || reuseRefreshToken);
    }

    /**
     * @Description 实现InitializingBean接口afterPropertiesSet()方法，所有的依赖注入完成后，Spring会调用其
     * afterPropertiesSet()方法，用于初始化bean之后进行特定的逻辑处理，执行其整体配置和最终初始化验证
     * 常用Bean初始化操作一般情况下执行顺序为：构造方法 -> Setter方法 -> @Value注解 -> @Autowired注解 ->
     * InitializingBean接口实现 -> @PostConstruct注解 -> BeanPostProcessor接口 -> ApplicationContextAware接口
     * 构造函数实现强制依赖，setter方法实现可选依赖
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        notNull(this.loginHandlerFactory, "loginHandlerFactory must be set");
        notNull(this.endpointConfig, "clientConfig must be set");
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        super.setClientDetailsService(clientService);
    }

    @Override
    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        super.setTokenStore(tokenStore);
        this.tokenStore = tokenStore;
        this.tokenRenewer = new TokenRenewer(tokenStore);
    }

    /**
     * 注销token
     */
    @Override
    public boolean revokeToken(String tokenValue) {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(tokenValue);
        if (null != accessToken) {
            revokeToken(accessToken);
            return TRUE;
        }
        return FALSE;
    }

    private void revokeToken(OAuth2AccessToken accessToken) {
        if (accessToken.getRefreshToken() != null) {
            this.tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        this.tokenStore.removeAccessToken(accessToken);
    }

    /**
     * 注销当前用户在所有端的token
     */
    public void revokeTokensByUser(String userName) {
        for (ClientProperties client : endpointConfig.getClients()) {
            Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(client.getClientId(), userName);
            if (isNotEmpty(tokens)) {
                tokens.forEach(this::revokeToken);
            }
        }
    }

    /**
     * 认证服务器自定义token校验
     */
    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            this.tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        } else {
            OAuth2Authentication authentication = this.tokenStore.readAuthentication(accessToken);
            if (authentication == null) {
                throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
            } else {
                //验证client
                ClientProperties clientProperties =
                        this.endpointConfig.getClientProperties(authentication.getOAuth2Request().getClientId());
                //token续签
                if (TRUE.equals(clientProperties.getRenewal())) {
                    this.tokenRenewer.execute(accessToken, authentication, clientProperties.getAccessTokenValidity());
                }
                return authentication;
            }
        }
    }

    @Autowired
    public void setEndpointConfig(EndpointConfig endpointConfig) {
        this.endpointConfig = endpointConfig;
    }

    @Autowired
    public void setLoginHandlerFactory(LoginHandlerFactory loginHandlerFactory) {
        this.loginHandlerFactory = loginHandlerFactory;
    }
}
