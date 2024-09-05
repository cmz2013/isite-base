package org.isite.sentinel.support;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.data.Result;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Constants.COLON;
import static org.isite.commons.lang.json.Jackson.toJsonString;

/**
 * @Description 自定义sentinel发生限流之后返回的参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class FallbackProvider {

    public Result<String> fallbackResponse(Throwable e) {
        if (e instanceof BlockException) {
            return new Result<>(SC_SERVICE_UNAVAILABLE, getBlockMessage((BlockException) e));
        } else {
            return new Result<>(SC_SERVICE_UNAVAILABLE, getMessage(e));
        }
    }

    private String getBlockMessage(BlockException e) {
        String message = null;
        if (e instanceof FlowException) {
            message = "sentinel flow exception";
        } else if (e instanceof DegradeException) {
            message = "sentinel degrade exception";
        } else if (e instanceof ParamFlowException) {
            message = "sentinel param flow exception";
        } else if (e instanceof SystemBlockException) {
            message = "sentinel system exception";
        } else if (e instanceof AuthorityException) {
            message = "sentinel authority exception";
        }
        message = isNotBlank(message) ? message : e.getClass().getSimpleName();
        if (null != e.getRule()) {
            message += COLON + toJsonString(e.getRule());
        }
        return message;
    }
}
