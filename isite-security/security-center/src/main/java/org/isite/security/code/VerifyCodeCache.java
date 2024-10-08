package org.isite.security.code;

import org.isite.security.data.enums.VerifyCodeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.isite.security.constants.SecurityConstants.VERIFY_CODE_VALIDITY;
import static org.isite.security.data.constants.CacheKey.VERIFY_CODE_FORMAT;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class VerifyCodeCache {
    /**
     * redis客户端
     */
    private StringRedisTemplate redisTemplate;

    /**
     * 保存验证码
     */
    public void saveCode(VerifyCodeMode verifyCodeMode, String agent, String code) {
        redisTemplate.opsForValue().set(format(VERIFY_CODE_FORMAT, verifyCodeMode.name(), agent), code, VERIFY_CODE_VALIDITY, MINUTES);
    }

    /**
     * 获取验证码
     */
    public String getCode(VerifyCodeMode verifyCodeMode, String agent) {
        return redisTemplate.opsForValue().get(format(VERIFY_CODE_FORMAT, verifyCodeMode.name(), agent));
    }

    /**
     * 删除验证码
     */
    public void deleteCode(VerifyCodeMode verifyCodeMode, String agent) {
        redisTemplate.delete(format(VERIFY_CODE_FORMAT, verifyCodeMode.name(), agent));
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
