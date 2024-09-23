package org.isite.commons.web.ftp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description FTP配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {
    private String host;
    private int port;
    private String userName;
    private String password;
}
