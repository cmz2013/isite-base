package org.isite.commons.cloud.converter;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;
import org.isite.commons.lang.enums.ResultStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResultConverter {

    private ResultConverter() {
    }

    public static Result<Object> toResult(Throwable throwable) {
        if (throwable instanceof Error) {
            Error error = (Error) throwable;
            return new Result<>(error.getCode(), MessageUtils.getMessage(error));
        }
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) throwable;
            return new Result<>(exception.getStatus().value(), exception.getMessage());
        }
        return new Result<>(ResultStatus.EXPECTATION_FAILED.getCode(), MessageUtils.getMessage(throwable));
    }
}
