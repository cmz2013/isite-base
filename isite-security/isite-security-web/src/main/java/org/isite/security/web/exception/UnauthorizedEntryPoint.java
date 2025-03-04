package org.isite.security.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.ContentType;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ResultStatus;
import org.isite.commons.lang.json.Jackson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description 自定义无效Token异常信息，用于未授权的用户请求非公共资源时返回信息
 * @Author <font color='blue'>zhangcm</font>
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.RESULT_CODE, ResultStatus.UNAUTHORIZED.getCode());
        map.put(Constants.RESULT_MESSAGE, null != e && StringUtils.isNotBlank(e.getMessage()) ?
                e.getMessage() : ResultStatus.UNAUTHORIZED.getReasonPhrase());
        response.setContentType(ContentType.APPLICATION_JSON);
        response.setStatus(ResultStatus.OK.getCode());
        response.getOutputStream().write(Jackson.toJsonString(map).getBytes());
    }
}
