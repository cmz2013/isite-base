package org.isite.security.data.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
//@AllArgsConstructor 不建议使用，因为字段顺序一旦调整，构造函数传参就会错误，如果类型匹配很容易忽略该错误
public class OauthClient {
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端名称
     */
    private String clientName;

    public OauthClient() {
    }

    public OauthClient(String clientId, String clientName) {
        this.clientId = clientId;
        this.clientName = clientName;
    }
}
