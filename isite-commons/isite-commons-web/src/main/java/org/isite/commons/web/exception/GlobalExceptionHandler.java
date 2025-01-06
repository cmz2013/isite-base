package org.isite.commons.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Error;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Constants.SEMICOLON;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @Description @ControllerAdvice主要用来处理全局数据，一般搭配@ExceptionHandler、@ModelAttribute以及@InitBinder使用
 * 1) @ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的
 * 2) @InitBinder注解标注的方法：用于请求中注册自定义参数的解析，从而达到自定义请求参数格式的目的
 * 3) @ModelAttribute注解标注的方法：表示此方法会在执行目标Controller方法之前执行
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Controller全局异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<Object> errorHandler(HttpServletResponse response, Exception e) {
        response.setStatus(OK.value());
        StringBuilder messages = new StringBuilder();
        if (e instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException) e).getConstraintViolations()) {
                messages.append(constraintViolation.getMessage()).append(SEMICOLON);
            }
            return new Result<>(BAD_REQUEST.value(), messages.toString());
        }
        if (e instanceof MethodArgumentNotValidException) {
            for (FieldError fieldError : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
                messages.append(fieldError.getDefaultMessage()).append(SEMICOLON);
            }
            return new Result<>(BAD_REQUEST.value(), messages.toString());
        }
        if (e instanceof Error) {
            return new Result<>(((Error) e).getCode(), getMessage(e));
        }
        return new Result<>(EXPECTATION_FAILED.value(), getMessage(e));
    }
}
