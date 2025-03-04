package org.isite.commons.web.exception;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;
import org.isite.commons.lang.enums.ResultStatus;
/**
 * @Description 非法参数异常
 * @Author <font color='blue'>zhangcm</font>
 */
public class IllegalParameterError extends Error {

    public IllegalParameterError() {
        super(ResultStatus.EXPECTATION_FAILED.getCode(),
                MessageUtils.getMessage("illegalParameter", "illegal parameters"));
    }
}
