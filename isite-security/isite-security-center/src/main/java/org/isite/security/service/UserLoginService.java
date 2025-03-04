package org.isite.security.service;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.commons.web.sign.SignSecret;
import org.isite.commons.web.utils.RequestUtils;
import org.isite.security.code.CaptchaExecutor;
import org.isite.security.code.CaptchaExecutorFactory;
import org.isite.security.constants.SecurityConstants;
import org.isite.security.converter.DataAuthorityConverter;
import org.isite.security.converter.UserConverter;
import org.isite.security.data.dto.UserRegistDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.enums.CaptchaType;
import org.isite.security.data.enums.CodeLoginMode;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.login.UserPasswordEncoder;
import org.isite.security.oauth.AuthenticationDetails;
import org.isite.security.oauth.TokenService;
import org.isite.security.web.utils.SecurityUtils;
import org.isite.tenant.client.RbacAccessor;
import org.isite.tenant.client.ResourceAccessor;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.isite.tenant.data.vo.Role;
import org.isite.user.client.UserAccessor;
import org.isite.user.data.constants.UserConstants;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class UserLoginService {
    private static final String BAD_SECRET = "incorrect username or %s";
    private SignSecret signSecret;
    private CaptchaExecutorFactory captchaExecutorFactory;
    private OauthUserService oauthUserService;
    private UserPasswordEncoder passwordMatcher;
    private TokenService tokenService;

    /**
     * 校验验证码，注册用户信息
     */
    public long registUser(UserRegistDto userRegistDto) {
        CaptchaExecutor captchaExecutor = captchaExecutorFactory.get(CaptchaType.SMS);
        Assert.isTrue(captchaExecutor.checkCaptcha(userRegistDto.getPhone(), userRegistDto.getCaptcha()),
                MessageUtils.getMessage("captcha.invalid", "the captcha is invalid"));
        userRegistDto.setPassword(passwordMatcher.encode(userRegistDto.getPassword()));
        return UserAccessor.addUser(UserConverter.toUserPostDto(userRegistDto), signSecret.password(UserConstants.SERVICE_ID));
    }

    /**
     * 校验验证码，更新用户密码
     */
    public int updatePassword(UserSecretDto userSecretDto) {
        UserSecret userSecret = UserAccessor.getUserSecret(
                userSecretDto.getUsername(), signSecret.password(UserConstants.SERVICE_ID));
        CaptchaType captchaType = userSecretDto.getCaptchaType();
        //核实用户的密保手机号或email地址
        Assert.notNull(userSecret, String.format(
                MessageUtils.getMessage("captcha.badSecret", BAD_SECRET), captchaType.getAgentLabel()));
        CaptchaExecutor captchaExecutor = captchaExecutorFactory.get(captchaType);
        Assert.isTrue(userSecretDto.getAgent().equals(captchaExecutor.getAgent(userSecret)),
                String.format(MessageUtils.getMessage("captcha.badSecret", BAD_SECRET), captchaType.getAgentLabel()));
        //校验验证码
        Assert.isTrue(captchaExecutor.checkCaptcha(userSecretDto.getAgent(), userSecretDto.getCaptcha()),
                MessageUtils.getMessage("captcha.invalid", "the captcha is invalid"));
        tokenService.revokeTokensByUser(userSecret.getUsername());
        return UserAccessor.updatePassword(userSecret.getId(),
                passwordMatcher.encode(userSecretDto.getPassword()),
                signSecret.password(UserConstants.SERVICE_ID));
    }

    /**
     * 获取登录用户客户端功能权限
     */
    public List<Role> getRoles(OauthUser oauthUser, String clientId) {
        if (Boolean.TRUE.equals(oauthUser.getInternal())) {
            Role role = new Role();
            role.setId(Constants.ZERO);
            role.setRoleName(TenantConstants.ROLE_ADMINISTRATOR);
            role.setResources(ResourceAccessor.getResources(clientId, signSecret.password(TenantConstants.SERVICE_ID)));
            return Collections.singletonList(role);
        }
        Rbac rbac = RbacAccessor.getRbac(
                new LoginDto(oauthUser.getTenant().getId(), oauthUser.getUserId(), clientId),
                signSecret.password(TenantConstants.SERVICE_ID));
        Assert.notNull(rbac, MessageUtils.getMessage("tenant.unavailable", "the tenant is unavailable"));
        oauthUser.setTenant(rbac.getTenant());
        oauthUser.setEmployeeId(rbac.getEmployeeId());
        oauthUser.setAuthorityMap(DataAuthorityConverter.toDataAuthority(rbac.getDataApis()));
        oauthUserService.storeOauthUser(SecurityUtils.getTokenValue(), oauthUser);
        return rbac.getRoles();
    }

    /**
     * 多租户的用户，登录以后可以切换租户，同时更新rbac权限信息
     */
    public List<Role> changeTenant(OauthUser oauthUser, Integer tenantId, String clientId) {
        Rbac rbac = RbacAccessor.getRbac(
                new LoginDto(tenantId, oauthUser.getUserId(), clientId),
                signSecret.password(TenantConstants.SERVICE_ID));
        Assert.notNull(rbac, MessageUtils.getMessage("tenant.unavailable", "the tenant is unavailable"));
        oauthUser.setTenant(rbac.getTenant());
        oauthUser.setEmployeeId(rbac.getEmployeeId());
        oauthUser.setAuthorityMap(DataAuthorityConverter.toDataAuthority(rbac.getDataApis()));
        oauthUserService.storeOauthUser(SecurityUtils.getTokenValue(), oauthUser);
        return rbac.getRoles();
    }

    /**
     * 获取clientId
     */
    public String getClientId(UsernamePasswordAuthenticationToken token) {
        /*
         * 授权码模式获取clientId
         * 访问/oauth/authorize接口请求授权码时，如果没有登录，则保存当前请求信息到session中，并重定向到登录页面。
         * 用户提交登录以后，会从session中获取之前的请求信息，构造AuthenticationDetails对象。
         */
        if (token.getDetails() instanceof AuthenticationDetails) {
            return ((AuthenticationDetails) token.getDetails()).getClientId();
        }
        //客户端http basic认证方式获取clientId
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    /**
     * 获取CODE_LOGIN_MODE参数
     */
    public CodeLoginMode getCodeLoginMode(UsernamePasswordAuthenticationToken token) {
        if (token.getDetails() instanceof AuthenticationDetails) {
            return ((AuthenticationDetails) token.getDetails()).getCodeLoginMode();
        }
        return Enumerable.getByCode(CodeLoginMode.class,
                RequestUtils.getRequest().getParameter(SecurityConstants.CODE_LOGIN_MODE));
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

    @Autowired
    public void setCaptchaExecutorFactory(CaptchaExecutorFactory captchaExecutorFactory) {
        this.captchaExecutorFactory = captchaExecutorFactory;
    }

    @Autowired
    public void setOauthUserService(OauthUserService oauthUserService) {
        this.oauthUserService = oauthUserService;
    }

    @Autowired
    public void setPasswordMatcher(UserPasswordEncoder passwordMatcher) {
        this.passwordMatcher = passwordMatcher;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
