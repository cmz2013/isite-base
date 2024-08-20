package org.isite.security.code;

import org.isite.security.data.enums.VerifyCodeMode;
import org.isite.user.data.vo.UserSecret;
import org.springframework.stereotype.Component;

import static org.isite.security.data.enums.VerifyCodeMode.SMS;

/**
 * @Description 发送验证码
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
//@ConditionalOnBean TODO 配置了SMS才注入SmsCodeService
public class SmsCodeHandler extends VerifyCodeHandler {

    public SmsCodeHandler() {
        super(SMS);
    }

    @Override
    protected boolean sendCode(String agent, String code) {
        return false; //TODO SmsClient 手机短信客户端
    }

    @Override
    public String getAgent(UserSecret userSecret) {
        return userSecret.getPhone();
    }

    @Override
    public VerifyCodeMode[] getIdentities() {
        return new VerifyCodeMode[] { SMS };
    }
}
