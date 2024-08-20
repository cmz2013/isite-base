package org.isite.security.web.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.data.Constants.RESULT_CODE;
import static org.isite.commons.lang.data.Constants.RESULT_MESSAGE;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @Description 自定义无效Token异常信息，用于未授权的用户请求非公共资源时返回信息
 * @Author <font color='blue'>zhangcm</font>
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(RESULT_CODE, UNAUTHORIZED.value());
        map.put(RESULT_MESSAGE, null != e && isNotBlank(e.getMessage()) ?
                e.getMessage() : UNAUTHORIZED.getReasonPhrase());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());
        response.getOutputStream().write(toJsonString(map).getBytes());
    }
}
