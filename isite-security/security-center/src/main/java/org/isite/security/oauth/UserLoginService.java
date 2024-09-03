package org.isite.security.oauth;

import org.isite.commons.web.sign.SignSecret;
import org.isite.security.code.VerifyCodeHandler;
import org.isite.security.code.VerifyCodeHandlerFactory;
import org.isite.security.data.dto.UserPostDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.enums.VerifyCodeMode;
import org.isite.user.client.UserAccessor;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.security.converter.UserConverter.toUserDto;
import static org.isite.security.data.enums.VerifyCodeMode.EMAIL;
import static org.isite.user.client.UserAccessor.addUser;
import static org.isite.user.client.UserAccessor.getUserSecret;
import static org.isite.user.data.constants.UserConstants.SERVICE_ID;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class UserLoginService {

    private static final String BAD_SECRET = "incorrect username or %s";
    private SignSecret signSecret;
    private VerifyCodeHandlerFactory verifyCodeHandlerFactory;
    private TokenService tokenService;

    /**
     * 校验验证码，注册用户信息
     */
    public Integer registUser(UserPostDto userPostDto) {
        String agent = userPostDto.getPhone();
        if (EMAIL.equals(userPostDto.getVerifyCodeMode())) {
            notBlank(userPostDto.getEmail(), "email cannot be null");
            agent = userPostDto.getEmail();
        }
        VerifyCodeHandler verifyCodeHandler = verifyCodeHandlerFactory.get(userPostDto.getVerifyCodeMode());
        isTrue(verifyCodeHandler.checkCode(agent, userPostDto.getCode()),
                getMessage("VerifyCode.invalid", "the verification code is invalid"));
        return addUser(toUserDto(userPostDto), signSecret.password(SERVICE_ID));
    }

    /**
     * 校验验证码，更新用户密码
     */
    public int updatePassword(UserSecretDto userSecretDto) {
        UserSecret userSecret = getUserSecret(userSecretDto.getUsername(), signSecret.password(SERVICE_ID));
        VerifyCodeMode verifyCodeMode = userSecretDto.getVerifyCodeMode();
        //核实用户的密保手机号或email地址
        notNull(userSecret, format(getMessage("VerifyCode.badSecret", BAD_SECRET), verifyCodeMode.getLabel()));
        VerifyCodeHandler verifyCodeHandler = verifyCodeHandlerFactory.get(verifyCodeMode);
        isTrue(userSecretDto.getAgent().equals(verifyCodeHandler.getAgent(userSecret)),
                format(getMessage("VerifyCode.badSecret", BAD_SECRET), verifyCodeMode.getLabel()));

        //校验验证码
        isTrue(verifyCodeHandler.checkCode(userSecretDto.getAgent(), userSecretDto.getCode()),
                getMessage("VerifyCode.invalid", "the verification code is invalid"));
        tokenService.revokeTokensByUser(userSecret.getUserName());
        return UserAccessor.updatePassword(userSecret.getUserId(),
                new BCryptPasswordEncoder().encode(userSecretDto.getPassword()),
                signSecret.password(SERVICE_ID));
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

    @Autowired
    public void setVerifyCodeHandlerFactory(VerifyCodeHandlerFactory verifyCodeHandlerFactory) {
        this.verifyCodeHandlerFactory = verifyCodeHandlerFactory;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
