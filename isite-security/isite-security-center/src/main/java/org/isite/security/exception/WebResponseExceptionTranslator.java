package org.isite.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
/**
 * @Description 获取token认证失败,个性化错误处理。HTTP响应状态为200
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        return new ResponseEntity<>(new AuthenticationException(e), HttpStatus.OK);
    }
}
