package org.isite.sentinel.web;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.isite.commons.cloud.data.constants.ContentType;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.enums.ResultStatus;
import org.isite.sentinel.support.FallbackProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Description server端统一限流降级返回值（Feign、RestTemplate等客户端调用限流时抛异常）
 * @Author <font color='blue'>zhangcm</font>
 */
public class BlockRequestHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        Result<String> result = new FallbackProvider().fallbackResponse(e);
        response.setStatus(ResultStatus.OK.getCode());
        response.setContentType(ContentType.APPLICATION_JSON);
        response.getWriter().write(result.toString());
    }
}
