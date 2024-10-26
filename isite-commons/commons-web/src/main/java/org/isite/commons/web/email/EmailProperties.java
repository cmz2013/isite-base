package org.isite.commons.web.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description email客户端配置类。
 * 在类上添加注解 @ConfigurationProperties，把类的属性与yml配置文件绑定起来，还需要加上@Component注解才能绑定并注入IOC容器中。
 * 也可以不加@Component注解，在配置类上添加注解@EnableConfigurationProperties(EmailProperties.class)，让使用了@ConfigurationProperties注解的类注入到IOC容器中。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "email")
public class EmailProperties {
    /**
     * 邮件服务器配置
     */
    private Smtp smtp;
    /**
     * 配置发件邮箱
     */
    protected From from;
    /**
     * 为SMTP传输启用SSL加密
     */
    private Boolean ssl;

    /**
     * 内部类必须是 public static，否则属性无法注入
     */
    @Getter
    @Setter
    public static class Smtp {
        private String host;
        private Integer port;
        /**
         * 秒
         */
        private Integer timeout;
    }

    @Getter
    @Setter
    public static class From {
        private String address;
        private String name;
        private Authentication authentication;

        @Getter
        @Setter
        public static class Authentication {
            private String username;
            private String password;
        }
    }
}
