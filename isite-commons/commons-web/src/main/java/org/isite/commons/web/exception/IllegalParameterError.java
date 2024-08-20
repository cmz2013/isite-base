package org.isite.commons.web.exception;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;

import static org.isite.commons.lang.http.HttpStatus.EXPECTATION_FAILED;

/**
 * @description 非法参数异常
 * @author <font color='blue'>zhangcm</font>
 */
public class IllegalParameterError extends Error {

    public IllegalParameterError() {
        super(EXPECTATION_FAILED.getCode(), MessageUtils.getMessage("illegalParameter", "illegal parameters"));
    }
}
