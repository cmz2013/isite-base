package org.isite.commons.web.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 在中国移动或其他运营商的平台上注册账号，并获取API密钥和相关参数。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    /**
     * 短信API接口地址
     */
    private String apiUrl;
    /**
     * API密钥
     */
    protected String apiKey;
    /**
     * API密钥参数名
     */
    private String fieldKey;
    /**
     * 短信发送接口接收短信的手机号码参数名
     */
    private String fieldMobile;
    /**
     * 短信发送接口短信内容参数名
     */
    private String fieldContent;
}
