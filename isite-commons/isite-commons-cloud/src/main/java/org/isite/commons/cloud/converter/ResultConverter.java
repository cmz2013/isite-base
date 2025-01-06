package org.isite.commons.cloud.converter;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Error;
import org.springframework.web.server.ResponseStatusException;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.enums.ResultStatus.EXPECTATION_FAILED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResultConverter {

    private ResultConverter() {
    }

    public static Result<Object> toResult(Throwable throwable) {
        if (throwable instanceof Error) {
            Error error = (Error) throwable;
            return new Result<>(error.getCode(), getMessage(error));
        }
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) throwable;
            return new Result<>(exception.getStatus().value(), exception.getMessage());
        }
        return new Result<>(EXPECTATION_FAILED.getCode(), getMessage(throwable));
    }
}
