package org.isite.commons.web.ftp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableConfigurationProperties(FtpProperties.class)
@ConditionalOnProperty({"ftp.host", "ftp.port"})
public class FtpConfig {

    @Bean
    public FtpClient emailClient(FtpProperties ftpProperties) {
        return new FtpClient(ftpProperties);
    }
}
