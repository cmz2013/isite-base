package org.isite.security.oauth;

import org.isite.security.data.enums.LoginCodeType;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.login.ClientLoginFactory;
import org.isite.security.login.CodeLoginFactory;
import org.isite.security.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.security.constants.SecurityConstants.LOGIN_FAILURE_TIMES_MAX;
import static org.isite.security.data.constants.CacheKey.LOGIN_LOCKED_FORMAT;
import static org.isite.security.data.constants.CacheKey.LOGIN_TIMES_FORMAT;
import static org.isite.security.data.constants.SecurityConstants.BAD_CREDENTIALS;

/**
 * @Description 处理基于用户名和密码的认证
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private CodeLoginFactory codeLoginFactory;
    private ClientLoginFactory clientLoginFactory;
    private StringRedisTemplate redisTemplate;
    private UserLoginService userLoginService;

    /**
     * @Description 验证用户身份
     * spring security异常消息默认是英文的，通过MessageSourceConfigurer配置成中文的且可以自定义异常消息。
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails user, UsernamePasswordAuthenticationToken token) {
        if (!user.isEnabled()) {
            throw new DisabledException(getMessage("user.disabled", "User is disabled"));
        }
        OauthUser oauthUser = cast(user);
        LoginCodeType codeType = userLoginService.getCodeType(token);
        if (null != codeType) {
            if (!codeLoginFactory.get(codeType).checkCode(token.getName(), token.getCredentials().toString())) {
                this.checkFailureTimes(user.getUsername());
                throw new BadCredentialsException(getMessage("verificationCode.invalid", BAD_CREDENTIALS));
            }
        } else {
            if (!clientLoginFactory.get(userLoginService.getClientId(token)).checksPassword(oauthUser.getPassword(), token)) {
                this.checkFailureTimes(oauthUser.getUsername());
                throw new BadCredentialsException(getMessage("user.badCredentials", BAD_CREDENTIALS));
            }
        }
    }

    /**
     * 检索用户信息
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) {
        if (isBlank(username) || null == token.getCredentials()) {
            throw new BadCredentialsException(getMessage("user.badCredentials", BAD_CREDENTIALS));
        }
        // username是用户名或手机号或邮箱地址等
        if (isNonLocked(username)) {
            LoginCodeType codeType = userLoginService.getCodeType(token);
            OauthUser oauthUser = null != codeType ?
                    codeLoginFactory.get(codeType).getOauthUser(username, token) :
                    clientLoginFactory.get(userLoginService.getClientId(token)).getOauthUser(username, token);
            if (null == oauthUser) {
                this.checkFailureTimes(username);
                //默认会自动隐藏UsernameNotFoundException异常，抛出BadCredentialsException
                throw new UsernameNotFoundException(username);
            }
            return oauthUser;
        }
        throw new LockedException(getMessage("user.locked",
                "The user's account is locked and they can try to log in again after 24 hours"));
    }

    /**
     * 判断账号没有被锁定
     */
    private boolean isNonLocked(String username) {
        return isBlank(redisTemplate.opsForValue().get(format(LOGIN_LOCKED_FORMAT, username))) ? TRUE : FALSE;
    }

    /**
     * 一日内错误了四次,然后锁定账号,第五次即使密码正确也会报账户锁定.
     * 为了防止暴力破解账号和密码，一般会对用户登录失败次数进行限定，在一定时间窗口超过一定次数，则锁定账户，来确保系统安全。
     */
    private void checkFailureTimes(String username) {
        String lockKey = format(LOGIN_LOCKED_FORMAT, username);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String lockValue = operations.get(lockKey);
        if (isBlank(lockValue) || isFalse(parseBoolean(lockValue))) {
            String timesKey = format(LOGIN_TIMES_FORMAT, username);
            Long timesValue = operations.increment(timesKey);
            if (null != timesValue && timesValue <= LOGIN_FAILURE_TIMES_MAX) {
                redisTemplate.expire(timesKey, DAY.getMillis(), MILLISECONDS);
            } else {
                operations.set(lockKey, TRUE.toString(), DAY.getMillis(), MILLISECONDS);
                redisTemplate.delete(timesKey);
            }
        }
    }

    @Autowired
    public void setCodeLoginFactory(CodeLoginFactory codeLoginFactory) {
        this.codeLoginFactory = codeLoginFactory;
    }

    @Autowired
    public void setClientLoginFactory(ClientLoginFactory clientLoginFactory) {
        this.clientLoginFactory = clientLoginFactory;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }
}
