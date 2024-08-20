package org.isite.security.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;

/**
 * @Author <font color='blue'>zhangcm</font>
 * @Date 2024/8/19 20:57
 */
@Configuration
@ConditionalOnProperty(name = "security.data.authority", havingValue = "true")
public class DataAuthorityConfig {

    @Bean
    public DataAuthorityAssert dataAuthorityAssert(
            @Value("${security.oauth2.permit}") String oauthPermits,
            @Value("${security.data.permit}") String dataPermits,
            PathMatcher pathMatcher) {
        DataAuthorityAssert dataAuthorityAssert = new DataAuthorityAssert(oauthPermits, dataPermits);
        dataAuthorityAssert.setPathMatcher(pathMatcher);
        return dataAuthorityAssert;
    }

}
