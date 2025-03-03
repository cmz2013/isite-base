package org.isite.fms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 微信支付配置参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "wxpay")
public class WxpayConfig {

    private String appId;
    private String mchId;
    private String mchKey;
}
