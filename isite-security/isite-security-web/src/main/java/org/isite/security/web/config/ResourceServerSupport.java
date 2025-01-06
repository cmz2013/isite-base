package org.isite.security.web.config;

import lombok.Setter;
import org.isite.security.web.exception.OverstepAccessHandler;
import org.isite.security.web.exception.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.isite.commons.lang.Constants.COMMA;

/** 
 * @Description 配置资源服务器。
 * ResourceServerConfigurerAdapter是默认情况下spring security oauth2的http配置，
 * 使用一个特殊的过滤器来检查请求中的承载令牌，以便通过 OAuth2 对请求进行认证。
 * @Author <font color='blue'>zhangcm</font>
 */
@Setter
public class ResourceServerSupport extends ResourceServerConfigurerAdapter {

    /**
     * @Description 不需要登录就可以访问的api白名单。
     * 如果@Value写在属性上，不需要提供setter方法
     */
    @Value("${security.oauth2.permit}")
    private String permit;

    @Bean
    public OverstepAccessHandler overstepAccessHandler() {
        return new OverstepAccessHandler();
    }

    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint() {
        return new UnauthorizedEntryPoint();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(isNotBlank(permit) ? split(permit, COMMA) : new String[] {}).permitAll()
                //除白名单外，其他任何请求都需要认证
                .anyRequest().authenticated()
                //自定义token空异常处理
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resource) {
        //自定义token无效异常处理
        resource.authenticationEntryPoint(unauthorizedEntryPoint())
                //已授权的用户请求权限之外的资源异常处理
                .accessDeniedHandler(overstepAccessHandler());
    }
}
