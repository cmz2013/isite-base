package org.isite.security.web.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.data.Constants.RESULT_CODE;
import static org.isite.commons.lang.data.Constants.RESULT_MESSAGE;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @Description 自定义异常信息，用于已授权的用户请求权限之外的资源时时返回信息
 * @Author <font color='blue'>zhangcm</font>
 */
public class OverstepAccessHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, Object> map = new HashMap<>();
        map.put(RESULT_CODE, FORBIDDEN.value());
        map.put(RESULT_MESSAGE, null != e && isNotBlank(e.getMessage()) ?
                e.getMessage() : FORBIDDEN.getReasonPhrase());
        response.setStatus(OK.value());
        response.getOutputStream().write(toJsonString(map).getBytes());
    }
}
