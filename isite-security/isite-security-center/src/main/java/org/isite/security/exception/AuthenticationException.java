package org.isite.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
/**
 * @Description 登录失败异常信息
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonSerialize(using = AuthenticationExceptionSerializer.class)
public class AuthenticationException extends OAuth2Exception {

    public AuthenticationException(Throwable t) {
        super(t.getMessage(), t);
    }
}
