package org.isite.commons.web.sms;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.lang.http.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.lang.data.Constants.THREE;

/**
 * @Description 发送短信的客户端
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Setter
public class SmsClient {
    /**
     * 短信API接口地址
     */
    private final String apiUrl;
    /**
     * API密钥
     */
    private final String apiKey;
    /**
     * API密钥参数名
     */
    private String keyField = "api_key";
    /**
     * 短信发送接口接收短信的手机号码参数名
     */
    private String mobileField = "mobile";
    /**
     * 短信发送接口短信内容参数名
     */
    private String contentField = "content";

    public SmsClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /**
     * @Description 发送短信
     * @param mobile 接收短信的手机号码
     * @param content 短信内容
     */
    public void send(String mobile, String content) throws IOException, URISyntaxException {
        Map<String, Object> params = new HashMap<>(THREE);
        params.put(keyField, apiKey);
        params.put(mobileField, mobile);
        params.put(contentField, content);
        HttpClient httpClient = new HttpClient();
        httpClient.post(apiUrl, params);
    }
}
