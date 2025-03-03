package org.isite.security.login;

import org.isite.security.code.CodeExecutorFactory;
import org.isite.security.data.enums.LoginCodeType;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.isite.security.data.enums.LoginCodeType.VERIFICATION_CODE_EMAIL;
import static org.isite.security.data.enums.VerificationCodeType.EMAIL;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class EmailCodeLogin implements CodeLogin {

    private ClientLoginFactory clientLoginFactory;
    private CodeExecutorFactory codeExecutorFactory;
    private UserLoginService userLoginService;

    @Override
    public LoginCodeType[] getIdentities() {
        return new LoginCodeType[] {VERIFICATION_CODE_EMAIL};
    }

    @Override
    public OauthUser getOauthUser(String username, UsernamePasswordAuthenticationToken token) {
        return clientLoginFactory.get(userLoginService.getClientId(token)).getOauthUser(username, token);
    }

    @Override
    public boolean checkCode(String agent, String code) {
        return codeExecutorFactory.get(EMAIL).checkCode(agent, code);
    }

    @Autowired
    public void setClientLoginFactory(ClientLoginFactory clientLoginFactory) {
        this.clientLoginFactory = clientLoginFactory;
    }

    @Autowired
    public void setCodeExecutorFactory(CodeExecutorFactory codeExecutorFactory) {
        this.codeExecutorFactory = codeExecutorFactory;
    }

    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }
}
