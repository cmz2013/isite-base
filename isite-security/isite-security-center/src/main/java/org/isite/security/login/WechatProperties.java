package org.isite.security.login;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 微信公众号配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    private String tokenUrl;
    private String appId;
    private String secret;
    private String userInfoUrl;
    /**
     * 获取手机号
     */
    private String phoneUrl;
}
