package org.isite.security.code;

import org.isite.security.data.enums.VerificationCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.isite.security.constants.SecurityConstants.VERIFICATION_CODE_VALIDITY;
import static org.isite.security.data.constants.CacheKey.VERIFICATION_CODE_FORMAT;

/**
 * @Description 短信/邮件验证码缓存
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class VerificationCodeCache {
    /**
     * redis客户端
     */
    private StringRedisTemplate redisTemplate;

    /**
     * 保存验证码
     */
    public void saveCode(VerificationCodeType codeType, String agent, String code) {
        redisTemplate.opsForValue().set(format(VERIFICATION_CODE_FORMAT, codeType.name(), agent),
                code, VERIFICATION_CODE_VALIDITY, MINUTES);
    }

    /**
     * 获取验证码
     */
    public String getCode(VerificationCodeType codeType, String agent) {
        return redisTemplate.opsForValue().get(format(VERIFICATION_CODE_FORMAT, codeType.name(), agent));
    }

    /**
     * 删除验证码
     */
    public void deleteCode(VerificationCodeType codeType, String agent) {
        redisTemplate.delete(format(VERIFICATION_CODE_FORMAT, codeType.name(), agent));
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
