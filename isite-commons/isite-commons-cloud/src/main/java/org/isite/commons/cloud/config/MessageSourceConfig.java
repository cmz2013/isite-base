package org.isite.commons.cloud.config;

import org.apache.commons.codec.CharEncoding;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @Description 在项目类路径（resource目录）下建立i18n文件夹，用于存放国际化messages文件。
 * i18n-cloud文件夹，是jar包提供的公共message文件，是为了避免覆盖项目classpath下的i18n文件夹。
 * Spring Boot应用程序的类路径包括以下几个主要部分：
 * 1)主应用程序类路径：这是包含主应用程序类（通常带有@SpringBootApplication注解）的目录。Spring Boot会从这个目录开始扫描组件和配置。
 * 2)依赖库：Spring Boot应用程序通常会依赖许多外部库。这些库通常打包在JAR文件中，并放在classpath中。
 * 3)资源文件：包括application.properties或application.yml配置文件、静态资源（如HTML、CSS、JavaScript文件）、模板文件等。
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("i18n/messages", "i18n-cloud/messages");
        messageSource.setDefaultEncoding(CharEncoding.UTF_8);
        return messageSource;
    }

    /**
     * MessageSourceAccessor是MessageSource的辅助类，根据Local读取资源文件
     */
    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }
}
