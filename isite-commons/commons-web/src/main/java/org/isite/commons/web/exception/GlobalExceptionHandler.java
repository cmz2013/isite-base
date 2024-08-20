package org.isite.commons.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.lang.data.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static org.isite.commons.web.exception.ExceptionConverter.toResult;
import static org.springframework.http.HttpStatus.OK;

/**
 * @Description @ControllerAdvice主要用来处理全局数据，一般搭配@ExceptionHandler、@ModelAttribute以及@InitBinder使用
 * 1) @ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的
 * 2) @InitBinder注解标注的方法：用于请求中注册自定义参数的解析，从而达到自定义请求参数格式的目的
 * 3) @ModelAttribute注解标注的方法：表示此方法会在执行目标Controller方法之前执行
 *
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
        return toResult(e);
    }
}
