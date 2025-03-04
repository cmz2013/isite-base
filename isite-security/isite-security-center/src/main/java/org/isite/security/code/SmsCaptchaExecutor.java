package org.isite.security.code;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.template.FreeMarker;
import org.isite.commons.web.sms.SmsClient;
import org.isite.commons.web.sms.SmsConfig;
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
@ConditionalOnBean(value = SmsConfig.class)
public class SmsCaptchaExecutor extends CaptchaExecutor {
    private final SmsClient smsClient;

    @Autowired
    public SmsCaptchaExecutor(SmsClient smsClient) {
        super(CaptchaType.SMS);
        this.smsClient = smsClient;
    }

    @Override
    protected boolean sendCaptcha(String agent, String code) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_CODE, code);
            data.put(FIELD_VALIDITY, SecurityConstants.CAPTCHA_VALIDITY);
            this.smsClient.send(agent, FreeMarker.process(
                    MessageUtils.getMessage("captcha.info", SecurityConstants.CAPTCHA_INFO), data));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
    }

    @Override
    public String getAgent(UserBasic userBasic) {
        return userBasic.getPhone();
    }

    @Override
    public CaptchaType[] getIdentities() {
        return new CaptchaType[] {CaptchaType.SMS};
    }
}
