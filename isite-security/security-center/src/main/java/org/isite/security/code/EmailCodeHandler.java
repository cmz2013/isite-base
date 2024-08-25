package org.isite.security.code;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.email.EmailClient;
import org.isite.commons.web.email.EmailConfig;
import org.isite.security.data.enums.VerifyCodeMode;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.data.Constants.SPACE;
import static org.isite.commons.lang.template.FreeMarker.process;
import static org.isite.security.constants.SecurityConstants.VERIFY_CODE_VALIDITY;
import static org.isite.security.data.enums.VerifyCodeMode.EMAIL;

/**
 * @Description 发送验证码
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
@ConditionalOnBean(value = EmailConfig.class)
public class EmailCodeHandler extends VerifyCodeHandler {

    private final EmailClient emailClient;

    @Autowired
    public EmailCodeHandler(EmailClient emailClient) {
        super(EMAIL);
        this.emailClient = emailClient;
    }

    @Override
    protected boolean sendCode(String agent, String code) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_CODE, code);
            data.put(FIELD_VALIDITY, VERIFY_CODE_VALIDITY);
            this.emailClient.sendEmail(agent,
                    emailClient.getFromName() + SPACE + getMessage("VerifyCode.subject", "Verification Code"),
                    process(getMessage("VerifyCode.info",
                            "${code} is your verification code, " +
                                    "please complete the verification within ${validity} minutes. " +
                                    "For your safety, do not leak easily."),
                            data));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getAgent(UserSecret userSecret) {
        return userSecret.getEmail();
    }

    @Override
    public VerifyCodeMode[] getIdentities() {
        return new VerifyCodeMode[] {EMAIL};
    }
}
