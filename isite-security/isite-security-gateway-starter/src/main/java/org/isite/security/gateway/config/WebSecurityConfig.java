package org.isite.security.gateway.config;

import org.isite.commons.cloud.utils.RequestPathMatcher;
import org.isite.security.gateway.client.OauthUserClient;
import org.isite.security.gateway.filter.WebSecurityFilter;
import org.isite.security.support.DataAuthorityAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;

/**
 * @Description SecurityConfig在DataAuthorityConfig之后加载。
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    @ConditionalOnMissingBean
    public PathMatcher pathMatcher() {
        return new RequestPathMatcher();
    }

    @Bean
    public WebSecurityFilter webSecurityFilter(
            OauthUserClient oauthUserClient, PathMatcher pathMatcher,
            @Value("${security.oauth2.permit}") String oauthPermits,
            @Autowired(required = false) DataAuthorityAssert dataAuthorityAssert) {
        WebSecurityFilter webSecurityFilter = new WebSecurityFilter(oauthPermits);
        webSecurityFilter.setOauthUserClient(oauthUserClient);
        //不能在配置类的其他@Bean方法中调用pathMatcher()，因为该方法添加了@ConditionalOnMissingBean注解，所以可能没有实例化
        webSecurityFilter.setPathMatcher(pathMatcher);
        webSecurityFilter.setDataAuthorityAssert(dataAuthorityAssert);
        return webSecurityFilter;
    }
}
