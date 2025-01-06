package org.isite.security.service;

import org.isite.security.data.vo.OauthUser;
import org.isite.security.oauth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class OauthUserService {

    private TokenService tokenService;

    /**
     * 更新oauthUser缓存
     */
    public void storeOauthUser(String token, OauthUser oauthUser) {
        OAuth2AccessToken accessToken = tokenService.readAccessToken(token);
        OAuth2Authentication authentication = tokenService.readAuthentication(accessToken);
        authentication.setDetails(oauthUser);
        tokenService.storeAccessToken(accessToken, authentication);
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
