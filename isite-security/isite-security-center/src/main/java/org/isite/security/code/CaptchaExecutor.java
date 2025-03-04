package org.isite.security.code;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.factory.Strategy;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.schedule.RandomScheduler;
import org.isite.security.constants.SecurityConstants;
import org.isite.security.data.enums.CaptchaType;
import org.isite.user.data.vo.UserBasic;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Description 验证码操作接口
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class CaptchaExecutor implements Strategy<CaptchaType> {
    protected static final String FIELD_CODE = "code";
    protected static final String FIELD_VALIDITY = "validity";
    private final CaptchaType captchaType;
    private CaptchaCache captchaCache;

    protected CaptchaExecutor(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }

    /**
     * 校验验证码
     */
    public boolean checkCaptcha(String agent, String captcha) {
        String cacheCode = captchaCache.getCode(captchaType, agent);
        if (StringUtils.isNotBlank(captcha) && captcha.equals(cacheCode)) {
            //验证码只能使用一次
            captchaCache.deleteCode(captchaType, agent);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 发送验证码（验证码有效期内不能重复发送）
     * @param agent 手机号或email地址等
     */
    public void sendCaptcha(String agent) {
        String captcha = captchaCache.getCode(captchaType, agent);
        if (StringUtils.isNotBlank(captcha)) {
            return;
        }
        captcha = generateCaptcha();
        if (sendCaptcha(agent, captcha)) {
            captchaCache.saveCaptcha(captchaType, agent, captcha);
        }
    }

    /**
     * 获取用户密保手机或Email
     * @param userBasic 用户信息
     * @return 用户密保终端
     */
    public abstract String getAgent(UserBasic userBasic);

    /**
     * 发送验证码
     * @param agent 用户密保终端
     * @param code 验证码
     * @return 是否发送成功
     */
    protected abstract boolean sendCaptcha(String agent, String code);

    /**
     * 生成验证码
     */
    private String generateCaptcha() {
        StringBuilder code = new StringBuilder();
        for (int i = Constants.ZERO; i < SecurityConstants.CAPTCHA_LENGTH; i++) {
            code.append(RandomScheduler.nextInt(Constants.TEN));
        }
        return code.toString();
    }

    @Autowired
    public void setCaptchaCache(CaptchaCache captchaCache) {
        this.captchaCache = captchaCache;
    }
}
