package org.isite.security.support;

import org.isite.commons.cloud.utils.RequestPathMatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@ConditionalOnProperty(name = "security.data.authority", havingValue = "true")
public class DataAuthorityConfig {

    @Bean
    @ConditionalOnMissingBean
    public PathMatcher pathMatcher() {
        return new RequestPathMatcher();
    }

    @Bean
    public DataAuthorityAssert dataAuthorityAssert(
            PathMatcher pathMatcher,
            @Value("${security.oauth2.permit}") String oauthPermits,
            @Value("${security.data.permit}") String dataPermits) {
        //不能在配置类的其他@Bean方法中调用pathMatcher()，因为该方法添加了@ConditionalOnMissingBean注解，所以可能没有实例化
        return new DataAuthorityAssert(pathMatcher, oauthPermits, dataPermits);
    }

}
