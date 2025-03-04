package org.isite.security.oauth;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.security.config.ClientProperties;
import org.isite.security.config.EndpointConfig;
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
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class TokenService extends DefaultTokenServices {
    private TokenRenewer tokenRenewer;
    private EndpointConfig endpointConfig;
    private TokenStore tokenStore;

    public TokenService() {
        super();
        super.setSupportRefreshToken(Boolean.TRUE);
    }

    /**
     * 更新access_token时同步更新refresh_token
     * 如果属性上使用了@Value注解，该注解可直接对属性赋值，@Value的值从配置文件中获取
     */
    @Value("${security.oauth2.reuseRefreshToken}")
    public void setReuseRefreshToken(Boolean reuseRefreshToken) {
        super.setReuseRefreshToken(null == reuseRefreshToken || reuseRefreshToken);
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
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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
    public void revokeTokensByUser(String username) {
        for (ClientProperties client : endpointConfig.getClients()) {
            Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(client.getClientId(), username);
            if (CollectionUtils.isNotEmpty(tokens)) {
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
            throw new InvalidTokenException("invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            this.tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("access token expired: " + accessTokenValue);
        } else {
            OAuth2Authentication authentication = this.tokenStore.readAuthentication(accessToken);
            if (authentication == null) {
                throw new InvalidTokenException("invalid access token: " + accessTokenValue);
            } else {
                //验证client
                ClientProperties clientProperties =
                        this.endpointConfig.getClientProperties(authentication.getOAuth2Request().getClientId());
                //token续签
                if (Boolean.TRUE.equals(clientProperties.getRenewal())) {
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

    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        return this.tokenStore.readAuthentication(accessToken);
    }

    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        this.tokenStore.storeAccessToken(accessToken, authentication);
    }
}
