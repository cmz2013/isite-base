package org.isite.security.oauth;

import org.isite.commons.lang.enums.ChronoUnit;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Date;
/**
 * @Description token续签
 * @Author <font color='blue'>zhangcm</font>
 */
public class TokenRenewer {
    /**
     * 存取token
     */
    private final TokenStore tokenStore;

    public TokenRenewer(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * @Description token续签，1分钟内不续签
     * @Author <font color='blue'>zhangcm</font>
     */
    public void execute(OAuth2AccessToken accessToken, OAuth2Authentication authentication, int validity) {
        long expirationTime = System.currentTimeMillis() + validity * ChronoUnit.SECOND.getMillis();
        if (expirationTime - accessToken.getExpiration().getTime() > ChronoUnit.MINUTE.getMillis()) {
            ((DefaultOAuth2AccessToken) accessToken).setExpiration(new Date(expirationTime));
            tokenStore.storeAccessToken(accessToken, authentication);
        }
    }
}
