package org.isite.security.code;

import org.isite.security.constants.SecurityConstants;
import org.isite.security.data.constants.CacheKeys;
import org.isite.security.data.enums.CaptchaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * @Description 短信/邮件验证码缓存
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class CaptchaCache {
    /**
     * redis客户端
     */
    private StringRedisTemplate redisTemplate;

    /**
     * 保存验证码
     */
    public void saveCaptcha(CaptchaType captchaType, String agent, String code) {
        redisTemplate.opsForValue().set(String.format(CacheKeys.CAPTCHA_FORMAT, captchaType.name(), agent),
                code, SecurityConstants.CAPTCHA_VALIDITY, TimeUnit.MINUTES);
    }

    /**
     * 获取验证码
     */
    public String getCode(CaptchaType captchaType, String agent) {
        return redisTemplate.opsForValue().get(String.format(CacheKeys.CAPTCHA_FORMAT, captchaType.name(), agent));
    }

    /**
     * 删除验证码
     */
    public void deleteCode(CaptchaType captchaType, String agent) {
        redisTemplate.delete(String.format(CacheKeys.CAPTCHA_FORMAT, captchaType.name(), agent));
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
