package org.isite.security.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.ContentType;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ResultStatus;
import org.isite.commons.lang.json.Jackson;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description 自定义异常信息，用于已授权的用户请求权限之外的资源时时返回信息
 * @Author <font color='blue'>zhangcm</font>
 */
public class OverstepAccessHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.RESULT_CODE, ResultStatus.FORBIDDEN.getCode());
        map.put(Constants.RESULT_MESSAGE, null != e && StringUtils.isNotBlank(e.getMessage()) ?
                e.getMessage() : ResultStatus.FORBIDDEN.getReasonPhrase());
        response.setStatus(ResultStatus.OK.getCode());
        response.getOutputStream().write(Jackson.toJsonString(map).getBytes());
    }
}
