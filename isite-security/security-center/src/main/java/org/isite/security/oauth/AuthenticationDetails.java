package org.isite.security.oauth;

import lombok.Getter;
import org.isite.security.data.enums.LoginCodeType;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.Enumerable.getByCode;
import static org.isite.security.constants.SecurityConstants.CLIENT_ID;
import static org.isite.security.constants.SecurityConstants.CODE_TYPE;
import static org.isite.security.constants.SecurityConstants.SPRING_SECURITY_SAVED_REQUEST;

/**
 * @Description 扩展 WebAuthenticationDetails数据。WebAuthenticationDetails 是一个用于存储与HTTP请求相关的认证细节的类。
 * 访问/oauth/authorize接口请求授权码时，如果没有登录，则保存当前请求信息到session中，并重定向到登录页面。
 * 用户提交登录以后，会从session中获取之前的请求信息，构造WebAuthenticationDetails对象。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class AuthenticationDetails extends WebAuthenticationDetails {

    private final String clientId;
    private final LoginCodeType loginCodeType;

    public AuthenticationDetails(HttpServletRequest request) {
        super(request);
        SavedRequest savedRequest = ((SavedRequest) request.getSession().getAttribute(SPRING_SECURITY_SAVED_REQUEST));
        this.clientId = savedRequest.getParameterValues(CLIENT_ID)[ZERO];
        String[] codeTypes = savedRequest.getParameterValues(CODE_TYPE);
        this.loginCodeType = isNotEmpty(codeTypes) ? getByCode(LoginCodeType.class, codeTypes[ZERO]) : null;
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