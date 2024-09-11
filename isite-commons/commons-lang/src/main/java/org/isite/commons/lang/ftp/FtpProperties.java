package org.isite.commons.lang.ftp;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description FTP配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class FtpProperties {
    private String host;
    private int port;
    private String userName;
    private String password;
}
