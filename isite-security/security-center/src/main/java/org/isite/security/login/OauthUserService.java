package org.isite.security.login;

import org.isite.commons.web.signature.SignatureSecret;
import org.isite.security.data.vo.OauthUser;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.isite.user.client.UserAccessor.getUserSecret;
import static org.isite.user.data.constants.UserConstants.SERVICE_ID;

/**
 * @Description 获取用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class OauthUserService extends UserDetailsService {

    private SignatureSecret signatureSecret;

    @Override
    public OauthUser getOauthUser(String username, String clientId) {
        UserSecret userSecret = getUserSecret(username, signatureSecret.password(SERVICE_ID));
        if (null == userSecret) {
            return null;
        }
        OauthUser user = new OauthUser();
        user.setUserId(userSecret.getUserId());
        user.setUserName(userSecret.getUserName());
        user.setPassword(userSecret.getPassword());
        user.setInternal(userSecret.getInternal());
        user.setClientId(clientId);
        return user;
    }

    @Autowired
    public void setSignSecret(SignatureSecret signatureSecret) {
        this.signatureSecret = signatureSecret;
    }

}
