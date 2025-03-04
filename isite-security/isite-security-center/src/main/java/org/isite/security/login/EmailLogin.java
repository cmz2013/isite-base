package org.isite.security.login;

import org.isite.security.code.CaptchaExecutorFactory;
import org.isite.security.data.enums.CaptchaType;
import org.isite.security.data.enums.CodeLoginMode;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class EmailLogin implements CodeLogin {
    private ClientLoginFactory clientLoginFactory;
    private CaptchaExecutorFactory captchaExecutorFactory;
    private UserLoginService userLoginService;

    @Override
    public CodeLoginMode[] getIdentities() {
        return new CodeLoginMode[] {CodeLoginMode.CAPTCHA_EMAIL};
    }

    @Override
    public OauthUser getOauthUser(String username, UsernamePasswordAuthenticationToken token) {
        return clientLoginFactory.get(userLoginService.getClientId(token)).getOauthUser(username, token);
    }

    @Override
    public boolean checkCode(String agent, String captcha) {
        return captchaExecutorFactory.get(CaptchaType.EMAIL).checkCaptcha(agent, captcha);
    }

    @Autowired
    public void setClientLoginFactory(ClientLoginFactory clientLoginFactory) {
        this.clientLoginFactory = clientLoginFactory;
    }

    @Autowired
    public void setCaptchaExecutorFactory(CaptchaExecutorFactory captchaExecutorFactory) {
        this.captchaExecutorFactory = captchaExecutorFactory;
    }

    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }
}
