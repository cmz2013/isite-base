package org.isite.fms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 支付宝配置参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

    private String appId;
    private String privateKey;
    private String publicKey;
    private String charset;
    private String signType;
}
