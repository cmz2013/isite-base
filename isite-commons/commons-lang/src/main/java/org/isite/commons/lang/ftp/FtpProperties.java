package org.isite.commons.lang.ftp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description FTP配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FtpProperties {

    private String host;
    private int port;
    private String userName;
    private String password;
}
