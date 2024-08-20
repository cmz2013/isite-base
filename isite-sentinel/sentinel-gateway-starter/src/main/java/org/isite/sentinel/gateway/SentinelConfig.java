package org.isite.sentinel.gateway;

import org.isite.sentinel.support.FallbackProvider;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager.setBlockHandler;
import static com.alibaba.csp.sentinel.config.SentinelConfig.APP_TYPE_PROP_KEY;
import static java.lang.System.setProperty;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

/**
 * @Description Sentinel Gateway 配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class SentinelConfig {

    static {
        //在Sentinel注册之前设置：网关应用类型
        setProperty(APP_TYPE_PROP_KEY, "1");
    }

    @PostConstruct
    public void init() {
        /*
         * 自定义逻辑处理被限流的请求，默认实现为DefaultBlockRequestHandler
         */
        setBlockHandler((exchange, ex) -> status(OK).contentType(APPLICATION_JSON)
                    .bodyValue(new FallbackProvider().fallbackResponse(ex).toString()));
    }
}
