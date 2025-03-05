package org.isite.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
/**
 * @Description 配置 Spring Session
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableRedisHttpSession
public class SpringSessionConfig {
    private final Environment environment;

    @Autowired
    public SpringSessionConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        String domain = environment.getProperty("spring.session.cookie.domain");
        if (StringUtils.isNotBlank(domain)) {
            defaultCookieSerializer.setDomainName(domain);
        }
        String path = environment.getProperty("spring.session.cookie.path");
        if (StringUtils.isNotBlank(path)) {
            defaultCookieSerializer.setCookiePath(path);
        }
        String name = environment.getProperty("spring.session.cookie.name");
        if (StringUtils.isNotBlank(name)) {
            defaultCookieSerializer.setCookieName(name);
        }
        defaultCookieSerializer.setUseBase64Encoding(Boolean.FALSE);
        return defaultCookieSerializer;
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver(){
        CookieHttpSessionIdResolver cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
        cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer());
        return cookieHttpSessionIdResolver;
    }
}
