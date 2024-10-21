package org.isite.security.login;

import org.isite.security.data.vo.OauthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.isite.security.data.constants.CacheKey.LOGIN_LOCKED_FORMAT;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class UserDetailsService {

    private StringRedisTemplate redisTemplate;

    /**
     * @Description 查询用户信息
     */
    public abstract OauthUser getOauthUser(String username, String clientId);

    /**
     * 判断账号没有被锁定
     */
    public boolean isNonLocked(String username) {
        return isBlank(redisTemplate.opsForValue().get(format(LOGIN_LOCKED_FORMAT, username))) ? TRUE : FALSE;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
