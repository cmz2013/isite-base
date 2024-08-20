package org.isite.security.gateway.config;

import org.isite.commons.cloud.utils.RequestPathMatcher;
import org.isite.security.gateway.client.OauthUserClient;
import org.isite.security.gateway.filter.SecurityFilter;
import org.isite.security.support.DataAuthorityAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;

/**
 * @Description SecurityConfig在DataAuthorityConfig之后加载。
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PathMatcher pathMatcher() {
        return new RequestPathMatcher();
    }

    @Bean
    public SecurityFilter securityFilter(
            @Value("${security.oauth2.permit}") String oauthPermits,
            OauthUserClient oauthUserClient,
            @Autowired(required = false) DataAuthorityAssert dataAuthorityAssert) {
        SecurityFilter securityFilter = new SecurityFilter(oauthPermits);
        securityFilter.setPathMatcher(pathMatcher());
        securityFilter.setOauthUserClient(oauthUserClient);
        securityFilter.setDataAuthorityAssert(dataAuthorityAssert);
        return securityFilter;
    }
}
