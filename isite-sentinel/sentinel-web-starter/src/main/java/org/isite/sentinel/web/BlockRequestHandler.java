package org.isite.sentinel.web;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.isite.commons.cloud.data.Result;
import org.isite.sentinel.support.FallbackProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.isite.commons.lang.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @Description server端统一限流降级返回值（Feign、RestTemplate等客户端调用限流时抛异常）
 * @Author <font color='blue'>zhangcm</font>
 */
public class BlockRequestHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        Result<String> result = new FallbackProvider().fallbackResponse(e);
        response.setStatus(OK.getCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(result.toString());
    }
}
