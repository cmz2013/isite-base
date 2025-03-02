package org.isite.security.oauth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.isite.security.data.constants.SecurityUrls.URL_LOGIN_FORM;

/**
 * @Description 定义授权码模式下，用户登录成功失败之后的操作
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        /*
         * get请求的转发会转发到get请求，post请求的转发，会转发到post请求。
         * /oauth/login/process使用post，所以 SecurityCenterConstants.API_LOGIN_FORM 支持post
         */
        super.setUseForward(true);
        super.setDefaultFailureUrl(URL_LOGIN_FORM + "?error=" + exception.getMessage());
        super.onAuthenticationFailure(request, response, exception);
    }
}
