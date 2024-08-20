package org.isite.security.config;

import org.isite.security.oauth.AuthenticationDetailsBuilder;
import org.isite.security.oauth.LoginFailureHandler;
import org.isite.security.oauth.LoginSuccessHandler;
import org.isite.security.oauth.LogoutSuccessHandler;
import org.isite.security.web.config.ResourceServerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import static org.isite.security.data.constants.UrlConstants.POST_LOGIN_PROCESS;
import static org.isite.security.data.constants.UrlConstants.URL_LOGIN_FORM;
import static org.isite.security.data.constants.UrlConstants.URL_OAUTH_LOGOUT;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

/**
 * @Description 资源服务器配置（认证鉴权中心也是资源服务器）
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerSupport {

    private LoginSuccessHandler loginSuccessHandler;
    private LoginFailureHandler loginFailureHandler;
    private LogoutSuccessHandler logoutSuccessHandler;
    private AuthenticationDetailsBuilder authenticationDetailsBuilder;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*
         * Spring Security默认不会创建Session，Spring oauth2授权码模式登录认证成功后重定向/oauth/authorize，
         * 如果没有Session会导致/oauth/authorize接口取不到认证信息，又重定向登录页面。
         */
        http.sessionManagement().sessionCreationPolicy(IF_REQUIRED);
        //CORS中预检请求，首先向另外一个域名的资源发送一个HTTP OPTIONS请求，判断实际发送的请求是否安全。
        http.authorizeRequests().antMatchers(OPTIONS).permitAll()
                //打开http basic认证方式
                .and().httpBasic()
                //关跨域保护
                .and().cors().disable();

        //Can't configure antMatchers after anyRequest
        super.configure(http);

        http.formLogin()
                .authenticationDetailsSource(authenticationDetailsBuilder)
                //loginPage和loginProcessingUrl不配置：默认都是/login
                .loginPage(URL_LOGIN_FORM)
                //post请求
                .loginProcessingUrl(POST_LOGIN_PROCESS)
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and().logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .logoutUrl(URL_OAUTH_LOGOUT).permitAll();
    }

    @Autowired
    public void setLoginSuccessHandler(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Autowired
    public void setLoginFailureHandler(LoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
    }

    @Autowired
    public void setLogoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Autowired
    public void setAuthenticationDetailsBuilder(AuthenticationDetailsBuilder authenticationDetailsBuilder) {
        this.authenticationDetailsBuilder = authenticationDetailsBuilder;
    }
}
