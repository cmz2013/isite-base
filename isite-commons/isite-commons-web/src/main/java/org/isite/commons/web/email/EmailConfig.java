package org.isite.commons.web.email;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description email配置类。
 * 在类上添加注解 @ConfigurationProperties，把类的属性与yml配置文件绑定起来，还需要加上@Component注解才能绑定并注入IOC容器中。
 * 在配置类上添加注解 @EnableConfigurationProperties，是让使用了@ConfigurationProperties注解的类注入到IOC容器中,不用再加上@Component
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
@ConditionalOnProperty({"email.smtp.host", "email.smtp.port", "email.from.address"})
public class EmailConfig {

    @Bean
    public EmailClient emailClient(EmailProperties emailProperties) {
        EmailClient emailClient = new EmailClient(
                emailProperties.getSmtp().getHost(), emailProperties.getSmtp().getPort(),
                emailProperties.getFrom().getAuthentication().getUsername(),
                emailProperties.getFrom().getAuthentication().getPassword(),
                emailProperties.getFrom().getAddress(), emailProperties.getFrom().getName());

        emailClient.setSsl(emailProperties.getSsl());
        emailClient.setTimeout(emailProperties.getSmtp().getTimeout());
        return emailClient;
    }
}
