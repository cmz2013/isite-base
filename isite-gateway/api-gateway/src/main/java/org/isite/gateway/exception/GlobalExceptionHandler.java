package org.isite.gateway.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.isite.commons.cloud.converter.Converter.toResult;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.fromSupplier;

/**
 * @Description Spring Cloud Gateway（WebFlux）中的全局异常处理不能直接用@ControllerAdvice来处理
 * ErrorWebExceptionHandler 是一个用于处理全局异常的接口，它可以处理来自 Spring WebFlux 的异常，并将其转换为响应。
 * GlobalExceptionHandler 实现了 ErrorWebExceptionHandler 接口，并设置其优先级为最高。
 * @Author <font color='blue'>zhangcm</font>
 */
public class GlobalExceptionHandler implements ErrorWebExceptionHandler, Ordered {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(OK);
        // header set_json响应
        response.getHeaders().setContentType(APPLICATION_JSON);
        //返回异常原因给前端
        return response.writeWith(fromSupplier(() -> response.bufferFactory().wrap(
                toJsonString(toResult(throwable)).getBytes())));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}