package org.isite.security.code;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.sms.SmsClient;
import org.isite.commons.web.sms.SmsConfig;
import org.isite.security.data.enums.VerificationCodeType;
import org.isite.user.data.vo.UserBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.template.FreeMarker.process;
import static org.isite.security.constants.SecurityConstants.VERIFICATION_CODE_INFO;
import static org.isite.security.constants.SecurityConstants.VERIFICATION_CODE_VALIDITY;
import static org.isite.security.data.enums.VerificationCodeType.SMS;

/**
 * @Description 发送验证码
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
@ConditionalOnBean(value = SmsConfig.class)
public class SmsCodeExecutor extends CodeExecutor {

    private final SmsClient smsClient;

    @Autowired
    public SmsCodeExecutor(SmsClient smsClient) {
        super(SMS);
        this.smsClient = smsClient;
    }

    @Override
    protected boolean sendCode(String agent, String code) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_CODE, code);
            data.put(FIELD_VALIDITY, VERIFICATION_CODE_VALIDITY);
            this.smsClient.send(agent, process(getMessage("verificationCode.info", VERIFICATION_CODE_INFO), data));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getAgent(UserBasic userBasic) {
        return userBasic.getPhone();
    }

    @Override
    public VerificationCodeType[] getIdentities() {
        return new VerificationCodeType[] {SMS};
    }
}
