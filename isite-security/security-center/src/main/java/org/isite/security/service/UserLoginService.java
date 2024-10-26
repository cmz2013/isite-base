package org.isite.security.service;

import org.isite.commons.web.sign.SignSecret;
import org.isite.security.code.CodeExecutor;
import org.isite.security.code.CodeExecutorFactory;
import org.isite.security.data.dto.UserRegistDto;
import org.isite.security.data.dto.UserSecretDto;
import org.isite.security.data.enums.LoginCodeType;
import org.isite.security.data.enums.VerificationCodeType;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.login.UserPasswordEncoder;
import org.isite.security.oauth.AuthenticationDetails;
import org.isite.security.oauth.TokenService;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.isite.tenant.data.vo.Role;
import org.isite.user.client.UserAccessor;
import org.isite.user.data.constants.UserConstants;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.Enumerable.getByCode;
import static org.isite.commons.web.utils.RequestUtils.getRequest;
import static org.isite.security.constants.SecurityConstants.CODE_TYPE;
import static org.isite.security.converter.DataAuthorityConverter.toDataAuthority;
import static org.isite.security.converter.UserConverter.toUserPostDto;
import static org.isite.security.data.enums.VerificationCodeType.SMS;
import static org.isite.security.web.utils.SecurityUtils.getTokenValue;
import static org.isite.tenant.client.RbacAccessor.getRbac;
import static org.isite.tenant.client.ResourceAccessor.getResources;
import static org.isite.tenant.data.constants.TenantConstants.ROLE_ADMINISTRATOR;
import static org.isite.user.client.UserAccessor.addUser;
import static org.isite.user.client.UserAccessor.getUserSecret;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class UserLoginService {

    private static final String BAD_SECRET = "incorrect username or %s";
    private SignSecret signSecret;
    private CodeExecutorFactory codeExecutorFactory;
    private OauthUserService oauthUserService;
    private UserPasswordEncoder passwordMatcher;
    private TokenService tokenService;

    /**
     * 校验验证码，注册用户信息
     */
    public Long registUser(UserRegistDto userRegistDto) {
        CodeExecutor codeExecutor = codeExecutorFactory.get(SMS);
        isTrue(codeExecutor.checkCode(userRegistDto.getPhone(), userRegistDto.getCode()),
                getMessage("verificationCode.invalid", "the verification code is invalid"));
        userRegistDto.setPassword(passwordMatcher.encode(userRegistDto.getPassword()));
        return addUser(toUserPostDto(userRegistDto), signSecret.password(UserConstants.SERVICE_ID));
    }

    /**
     * 校验验证码，更新用户密码
     */
    public int updatePassword(UserSecretDto userSecretDto) {
        UserSecret userSecret = getUserSecret(userSecretDto.getUsername(), signSecret.password(UserConstants.SERVICE_ID));
        VerificationCodeType verificationCodeType = userSecretDto.getVerificationCodeType();
        //核实用户的密保手机号或email地址
        notNull(userSecret, format(getMessage("verificationCode.badSecret", BAD_SECRET), verificationCodeType.getAgentLabel()));
        CodeExecutor codeExecutor = codeExecutorFactory.get(verificationCodeType);
        isTrue(userSecretDto.getAgent().equals(codeExecutor.getAgent(userSecret)),
                format(getMessage("verificationCode.badSecret", BAD_SECRET), verificationCodeType.getAgentLabel()));
        //校验验证码
        isTrue(codeExecutor.checkCode(userSecretDto.getAgent(), userSecretDto.getCode()),
                getMessage("verificationCode.invalid", "the verification code is invalid"));
        tokenService.revokeTokensByUser(userSecret.getUsername());
        return UserAccessor.updatePassword(userSecret.getId(),
                passwordMatcher.encode(userSecretDto.getPassword()),
                signSecret.password(UserConstants.SERVICE_ID));
    }

    /**
     * 获取登录用户客户端功能权限
     */
    public List<Role> getRoles(OauthUser oauthUser, String clientId) {
        if (TRUE.equals(oauthUser.getInternal())) {
            Role role = new Role();
            role.setId(ZERO);
            role.setRoleName(ROLE_ADMINISTRATOR);
            role.setResources(getResources(clientId, signSecret.password(TenantConstants.SERVICE_ID)));
            return singletonList(role);
        }
        Rbac rbac = getRbac(
                new LoginDto(oauthUser.getTenant().getId(), oauthUser.getUserId(), clientId),
                signSecret.password(TenantConstants.SERVICE_ID));
        notNull(rbac, getMessage("tenant.unavailable", "tenant unavailable"));
        oauthUser.setTenant(rbac.getTenant());
        oauthUser.setEmployeeId(rbac.getEmployeeId());
        oauthUser.setAuthorityMap(toDataAuthority(rbac.getDataApis()));
        oauthUserService.storeOauthUser(getTokenValue(), oauthUser);
        return rbac.getRoles();
    }

    /**
     * 多租户的用户，登录以后可以切换租户，同时更新rbac权限信息
     */
    public List<Role> changeTenant(OauthUser oauthUser, Integer tenantId, String clientId) {
        Rbac rbac = getRbac(
                new LoginDto(tenantId, oauthUser.getUserId(), clientId),
                signSecret.password(TenantConstants.SERVICE_ID));
        notNull(rbac, getMessage("tenant.unavailable", "tenant unavailable"));
        oauthUser.setTenant(rbac.getTenant());
        oauthUser.setEmployeeId(rbac.getEmployeeId());
        oauthUser.setAuthorityMap(toDataAuthority(rbac.getDataApis()));
        oauthUserService.storeOauthUser(getTokenValue(), oauthUser);
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
        return ((User) getContext().getAuthentication().getPrincipal()).getUsername();
    }

    /**
     * 获取code_type参数
     */
    public LoginCodeType getCodeType(UsernamePasswordAuthenticationToken token) {
        if (token.getDetails() instanceof AuthenticationDetails) {
            return ((AuthenticationDetails) token.getDetails()).getLoginCodeType();
        }
        return getByCode(LoginCodeType.class, getRequest().getParameter(CODE_TYPE));
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

    @Autowired
    public void setCodeExecutorFactory(CodeExecutorFactory codeExecutorFactory) {
        this.codeExecutorFactory = codeExecutorFactory;
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
