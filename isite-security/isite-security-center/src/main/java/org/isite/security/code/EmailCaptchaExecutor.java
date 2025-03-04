package org.isite.security.code;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.template.FreeMarker;
import org.isite.commons.web.email.EmailClient;
import org.isite.commons.web.email.EmailConfig;
import org.isite.security.constants.SecurityConstants;
import org.isite.security.data.enums.CaptchaType;
import org.isite.user.data.vo.UserBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
/**
 * @Description 发送验证码
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
@ConditionalOnBean(value = EmailConfig.class)
public class EmailCaptchaExecutor extends CaptchaExecutor {
    private final EmailClient emailClient;

    @Autowired
    public EmailCaptchaExecutor(EmailClient emailClient) {
        super(CaptchaType.EMAIL);
        this.emailClient = emailClient;
    }

    @Override
    protected boolean sendCaptcha(String agent, String code) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_CODE, code);
            data.put(FIELD_VALIDITY, SecurityConstants.CAPTCHA_VALIDITY);
            this.emailClient.sendEmail(agent, emailClient.getFromName() + Constants.SPACE +
                            MessageUtils.getMessage("captcha.subject", "Captcha"),
                    FreeMarker.process(MessageUtils.getMessage("captcha.info", SecurityConstants.CAPTCHA_INFO), data));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getAgent(UserBasic userBasic) {
        return userBasic.getEmail();
    }

    @Override
    public CaptchaType[] getIdentities() {
        return new CaptchaType[] {CaptchaType.EMAIL};
    }
}
