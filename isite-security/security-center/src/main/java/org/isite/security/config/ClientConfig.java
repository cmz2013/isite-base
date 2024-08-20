package org.isite.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.List;

/**
 * @Description OAuth2 客户端配置
 * 在配置类上添加注解 @ConfigurationProperties，把配置类的属性与yml配置文件绑定起来
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.oauth2.endpoint")
public class ClientConfig {
    /**
     * 客户端配置信息，注意：变量名和配置文件保持一致
     */
    private List<ClientProperties> clients;

    /**
     * 获取客户端配置
     */
    public ClientProperties getClientProperties(String clientId) {
        for (ClientProperties client : clients) {
            if (client.getClientId().equals(clientId)) {
                return client;
            }
        }
        throw new NoSuchClientException("No client with requested id: " + clientId);
    }
}
