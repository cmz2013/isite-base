package org.isite.commons.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.lang.Error;
import org.isite.commons.lang.data.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class ExceptionConverter {

    private ExceptionConverter() {
    }

    public static Result<Object> toResult(Exception e) {
        if (e instanceof ConstraintViolationException) {
            Iterator<ConstraintViolation<?>> iterator =
                    ((ConstraintViolationException) e).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                return new Result<>(BAD_REQUEST.value(), iterator.next().getMessage());
            }
        }
        if (e instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            if (null != fieldError) {
                return new Result<>(BAD_REQUEST.value(), fieldError.getDefaultMessage());
            }
        }
        if (e instanceof Error) {
            return new Result<>(((Error) e).getCode(), getMessage(e));
        }
        return new Result<>(EXPECTATION_FAILED.value(), getMessage(e));
    }
}
