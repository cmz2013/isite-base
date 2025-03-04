package org.isite.security.oauth;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.security.constants.SecurityConstants;
import org.isite.security.data.enums.CodeLoginMode;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
/**
 * @Description 扩展 WebAuthenticationDetails数据。WebAuthenticationDetails 是一个用于存储与HTTP请求相关的认证细节的类。
 * 访问/oauth/authorize接口请求授权码时，如果没有登录，则保存当前请求信息到session中，并重定向到登录页面。
 * 用户提交登录以后，会从session中获取之前的请求信息，构造WebAuthenticationDetails对象。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class AuthenticationDetails extends WebAuthenticationDetails {
    private final String clientId;
    private final CodeLoginMode codeLoginMode;

    public AuthenticationDetails(HttpServletRequest request) {
        super(request);
        SavedRequest savedRequest = ((SavedRequest) request.getSession()
                .getAttribute(SecurityConstants.SPRING_SECURITY_SAVED_REQUEST));
        this.clientId = savedRequest.getParameterValues(SecurityConstants.CLIENT_ID)[Constants.ZERO];
        String[] codeLoginModes = savedRequest.getParameterValues(SecurityConstants.CODE_LOGIN_MODE);
        this.codeLoginMode = ArrayUtils.isNotEmpty(codeLoginModes) ?
                Enumerable.getByCode(CodeLoginMode.class, codeLoginModes[Constants.ZERO]) : null;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}