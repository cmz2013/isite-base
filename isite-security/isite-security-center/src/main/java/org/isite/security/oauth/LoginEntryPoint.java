package org.isite.security.oauth;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static java.lang.Boolean.TRUE;
import static org.isite.security.data.constants.UrlConstants.URL_LOGIN_FORM;

/**
 * @Description 当ExceptionTranslationFilter截获AuthenticationException或者AccessDeniedException异常时，
 * 就会调用AuthenticationEntryPoint的commence，AuthenticationEntryPoint默认实现是LoginUrlAuthenticationEntryPoint, 该类的处理是转发或重定向到登录页面
 * @Author <font color='blue'>zhangcm</font>
 */
public class LoginEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public LoginEntryPoint() {
        super(URL_LOGIN_FORM);
        /*
         * 转发到登录页面,request对象（session）的信息不会丢失
         * get请求的转发会转发到get请求，post请求的转发，会转发到post请求。
         * /oauth/authorize使用get，所以/oauth/login/form支持get
         */
        setUseForward(TRUE);
    }
}
