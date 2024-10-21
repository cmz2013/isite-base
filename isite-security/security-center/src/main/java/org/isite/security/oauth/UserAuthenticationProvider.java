package org.isite.security.oauth;

import org.isite.security.code.CodeHandler;
import org.isite.security.code.CodeHandlerFactory;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.login.LoginHandlerFactory;
import org.isite.security.login.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.web.utils.RequestUtils.getRequest;
import static org.isite.security.constants.SecurityConstants.LOGIN_FAILURE_TIMES_MAX;
import static org.isite.security.data.constants.CacheKey.LOGIN_LOCKED_FORMAT;
import static org.isite.security.data.constants.CacheKey.LOGIN_TIMES_FORMAT;
import static org.isite.security.data.constants.SecurityConstants.BAD_CREDENTIALS;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * @Description 处理基于用户名和密码的认证
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private StringRedisTemplate redisTemplate;
    private LoginHandlerFactory loginHandlerFactory;
    private CodeHandlerFactory codeHandlerFactory;

    /**
     * @Description 验证用户身份
     * spring security异常消息默认是英文的，通过MessageSourceConfigurer配置成中文的且可以自定义异常消息。
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails user, UsernamePasswordAuthenticationToken token) {
        if (token.getCredentials() == null) {
            throw new BadCredentialsException(getMessage("user.badCredentials", BAD_CREDENTIALS));
        }
        OauthUser oauthUser = cast(user);
        String credentials = token.getCredentials().toString();
        /*
         * 如果code_mode不为空，则username为手机号或邮箱，password为验证码
         */
        CodeHandler codeHandler = codeHandlerFactory.get(getRequest().getParameter("code_mode"));
        if (null != codeHandler) {
            if (!codeHandler.checkCode(getRequest().getParameter("username"), credentials)) {
                checkFailureTimes(oauthUser.getUsername());
                throw new BadCredentialsException(getMessage("code.invalid", BAD_CREDENTIALS));
            }
        } else {
            if (!loginHandlerFactory.getLoginHandler(oauthUser.getClientId()).getPasswordMatcher().matches(credentials, oauthUser)) {
                checkFailureTimes(oauthUser.getUsername());
                throw new BadCredentialsException(getMessage("user.badCredentials", BAD_CREDENTIALS));
            }
        }
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

    /**
     * 检索用户信息
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) {
        String clientId = getClientId(token);
        UserDetailsService userDetailsService = loginHandlerFactory.getLoginHandler(clientId).getUserDetailsService();
        if (userDetailsService.isNonLocked(username)) {
            OauthUser oauthUser = userDetailsService.getOauthUser(username, clientId);
            if (null == oauthUser) {
                checkFailureTimes(username);
                //默认会自动隐藏UsernameNotFoundException异常，抛出BadCredentialsException
                throw new UsernameNotFoundException(username);
            }
            return oauthUser;
        }
        throw new LockedException(getMessage("user.locked", "User account is locked"));
    }

    /**
     * 获取clientId
     */
    private String getClientId(UsernamePasswordAuthenticationToken token) {
        /*
         * 授权码模式获取clientId
         * 访问/oauth/authorize接口请求授权码时，如果没有登录，则保存当前请求信息到session中，并重定向到登录页面。
         * 用户提交登录以后，会从session中获取之前的请求信息，构造AuthenticationDetails对象。
         */
        if (token.getDetails() instanceof AuthenticationDetails) {
            return ((AuthenticationDetails) token.getDetails()).getClientId();
        }
        //客户端http basic认证方式获取clientId
        return ((User) getContext().getAuthentication().getPrincipal()).getUsername();
    }

    @Autowired
    public void setLoginHandlerFactory(LoginHandlerFactory loginHandlerFactory) {
        this.loginHandlerFactory = loginHandlerFactory;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setCodeHandlerFactory(CodeHandlerFactory codeHandlerFactory) {
        this.codeHandlerFactory = codeHandlerFactory;
    }
}
