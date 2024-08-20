package org.isite.security.config;

import org.isite.security.exception.WebResponseExceptionTranslator;
import org.isite.security.oauth.ClientService;
import org.isite.security.oauth.OauthTokenEnhancer;
import org.isite.security.oauth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import static org.isite.security.data.constants.CacheKey.SECURITY_PREFIX;
import static org.isite.security.data.constants.UrlConstants.URL_OAUTH;
import static org.isite.security.data.constants.UrlConstants.URL_OAUTH_APPROVAL;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * @Description 认证服务器配置
 * 被@Configuration注解的类内部包含有一个或多个被@Bean注解的方法，这些方法将会被会被Spring AOP代理增强，
 * 根据方法名构建bean定义，初始化Spring容器。被@Bean注解的方法"在初始化过程中"被多次调用时，也只会执行一次
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {
    private final RedisConnectionFactory redisConnectionFactory;
    private TokenService tokenService;
    private ClientService clientService;
    private WebResponseExceptionTranslator webResponseExceptionTranslator;
    private OauthTokenEnhancer oauthTokenEnhancer;
    private AuthenticationManager authenticationManager;

    @Autowired
    public OauthServerConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setWebResponseExceptionTranslator(WebResponseExceptionTranslator webResponseExceptionTranslator) {
        this.webResponseExceptionTranslator = webResponseExceptionTranslator;
    }

    @Autowired
    public void setOauthTokenEnhancer(OauthTokenEnhancer oauthTokenEnhancer) {
        this.oauthTokenEnhancer = oauthTokenEnhancer;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(SECURITY_PREFIX);
        return redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(redisTokenStore())
                .tokenServices(tokenService)
                // 个性化错误处理
                .exceptionTranslator(webResponseExceptionTranslator)
                // 自定义token生成方式
                .tokenEnhancer(oauthTokenEnhancer)
                // 支持GET、POST请求获取token
                .allowedTokenEndpointRequestMethods(POST, GET)
                // 替换默认的url
                .pathMapping(URL_OAUTH + "/confirm_access", URL_OAUTH_APPROVAL)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 让/oauth/token支持client_id以及client_secret作登录认证
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientService);
    }
}
