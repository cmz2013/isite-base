package org.isite.sentinel.support;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ResultStatus;
import org.isite.commons.lang.json.Jackson;

/**
 * @Description 自定义sentinel发生限流之后返回的参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class FallbackProvider {

    public Result<String> fallbackResponse(Throwable e) {
        if (e instanceof BlockException) {
            return new Result<>(ResultStatus.SERVICE_UNAVAILABLE.getCode(), getBlockMessage((BlockException) e));
        } else {
            return new Result<>(ResultStatus.SERVICE_UNAVAILABLE.getCode(), MessageUtils.getMessage(e));
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
        message = StringUtils.isNotBlank(message) ? message : e.getClass().getSimpleName();
        if (null != e.getRule()) {
            message += Constants.COLON + Jackson.toJsonString(e.getRule());
        }
        return message;
    }
}
