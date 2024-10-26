package org.isite.security.login;

import org.isite.commons.web.sign.SignSecret;
import org.isite.security.data.enums.ClientIdentifier;
import org.isite.security.data.vo.OauthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.isite.security.converter.UserConverter.toOauthUser;
import static org.isite.user.client.UserAccessor.getUserSecret;
import static org.isite.user.data.constants.UserConstants.SERVICE_ID;

/**
 * @Description 用户中心登录
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserClientLogin implements ClientLogin {

    private UserPasswordEncoder userPasswordEncoder;
    private SignSecret signSecret;

    @Override
    public OauthUser getOauthUser(String username, UsernamePasswordAuthenticationToken token) {
        return toOauthUser(getUserSecret(username, signSecret.password(SERVICE_ID)));
    }

    @Override
    public boolean checksPassword(String password, UsernamePasswordAuthenticationToken token) {
        return userPasswordEncoder.matches(token.getCredentials().toString(), password);
    }

    @Autowired
    public void setUserPasswordEncoder(UserPasswordEncoder userPasswordEncoder) {
        this.userPasswordEncoder = userPasswordEncoder;
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

    @Override
    public ClientIdentifier[] getIdentities() {
        //不会注册到LoginHandlerFactory工厂类中
        return null;
    }
}
