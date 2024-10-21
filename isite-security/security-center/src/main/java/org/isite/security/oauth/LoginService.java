package org.isite.security.oauth;

import org.isite.commons.web.sign.SignSecret;
import org.isite.security.code.CodeHandler;
import org.isite.security.code.CodeHandlerFactory;
import org.isite.security.data.dto.UserRegistDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.enums.CodeMode;
import org.isite.security.login.BCryptMatcher;
import org.isite.user.client.UserAccessor;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.security.converter.UserConverter.toUserPostDto;
import static org.isite.security.data.enums.CodeMode.EMAIL;
import static org.isite.user.client.UserAccessor.addUser;
import static org.isite.user.client.UserAccessor.getUserSecret;
import static org.isite.user.data.constants.UserConstants.SERVICE_ID;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class LoginService {

    private static final String BAD_SECRET = "incorrect username or %s";
    private SignSecret signSecret;
    private CodeHandlerFactory codeHandlerFactory;
    private TokenService tokenService;
    private BCryptMatcher passwordMatcher;

    /**
     * 校验验证码，注册用户信息
     */
    public Integer registUser(UserRegistDto userRegistDto) {
        String agent = userRegistDto.getPhone();
        if (EMAIL.equals(userRegistDto.getCodeMode())) {
            notBlank(userRegistDto.getEmail(), "email cannot be null");
            agent = userRegistDto.getEmail();
        }
        CodeHandler codeHandler = codeHandlerFactory.get(userRegistDto.getCodeMode());
        isTrue(codeHandler.checkCode(agent, userRegistDto.getCode()),
                getMessage("code.invalid", "the verification code is invalid"));
        userRegistDto.setPassword(passwordMatcher.encode(userRegistDto.getPassword()));
        return addUser(toUserPostDto(userRegistDto), signSecret.password(SERVICE_ID));
    }

    /**
     * 校验验证码，更新用户密码
     */
    public int updatePassword(UserSecretDto userSecretDto) {
        UserSecret userSecret = getUserSecret(userSecretDto.getUsername(), signSecret.password(SERVICE_ID));
        CodeMode codeMode = userSecretDto.getCodeMode();
        //核实用户的密保手机号或email地址
        notNull(userSecret, format(getMessage("code.badSecret", BAD_SECRET), codeMode.getLabel()));
        CodeHandler codeHandler = codeHandlerFactory.get(codeMode);
        isTrue(userSecretDto.getAgent().equals(codeHandler.getAgent(userSecret)),
                format(getMessage("code.badSecret", BAD_SECRET), codeMode.getLabel()));
        //校验验证码
        isTrue(codeHandler.checkCode(userSecretDto.getAgent(), userSecretDto.getCode()),
                getMessage("code.invalid", "the verification code is invalid"));
        tokenService.revokeTokensByUser(userSecret.getUserName());
        return UserAccessor.updatePassword(userSecret.getUserId(),
                passwordMatcher.encode(userSecretDto.getPassword()),
                signSecret.password(SERVICE_ID));
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

    @Autowired
    public void setCodeHandlerFactory(CodeHandlerFactory codeHandlerFactory) {
        this.codeHandlerFactory = codeHandlerFactory;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setPasswordMatcher(BCryptMatcher passwordMatcher) {
        this.passwordMatcher = passwordMatcher;
    }
}
