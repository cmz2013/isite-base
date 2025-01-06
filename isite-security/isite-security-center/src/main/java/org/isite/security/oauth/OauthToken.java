package org.isite.security.oauth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @Description 自定义/oauth/token响应数据
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonSerialize(using = OauthTokenSerializer.class)
public class OauthToken extends DefaultOAuth2AccessToken {

    public OauthToken(OAuth2AccessToken accessToken) {
        super(accessToken);
    }
}
