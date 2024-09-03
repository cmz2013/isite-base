package org.isite.commons.web.sms;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @Description 短信服务配置类。
 * 在类上添加注解 @ConfigurationProperties，把类的属性与yml配置文件绑定起来，还需要加上@Component注解才能绑定并注入IOC容器中。
 * 在配置类上添加注解 @EnableConfigurationProperties，是让使用了@ConfigurationProperties注解的类注入到IOC容器中,不用再加上@Component
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty({"sms.apiUrl", "sms.apiKey"})
public class SmsConfig {

    @Bean
    public SmsClient smsClient(SmsProperties smsProperties) {
        SmsClient smsClient = new SmsClient(smsProperties.getApiUrl(), smsProperties.getApiKey());
        if (isNotBlank(smsProperties.getMobileField())) {
            smsClient.setFieldMobile(smsProperties.getMobileField());
        }
        if (isNotBlank(smsProperties.getContentField())) {
            smsClient.setFieldContent(smsProperties.getContentField());
        }
        if (isNotBlank(smsProperties.getKeyField())) {
            smsClient.setFieldKey(smsProperties.getKeyField());
        }
        return smsClient;
    }
}
