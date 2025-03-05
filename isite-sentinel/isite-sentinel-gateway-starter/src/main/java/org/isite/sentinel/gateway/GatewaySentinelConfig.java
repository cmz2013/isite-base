package org.isite.sentinel.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import org.isite.commons.lang.Constants;
import org.isite.sentinel.support.FallbackProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
/**
 * @Description Sentinel Gateway 配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class GatewaySentinelConfig {

    static {
        //在Sentinel注册之前设置：网关应用类型
        System.setProperty(SentinelConfig.APP_TYPE_PROP_KEY, String.valueOf(Constants.ONE));
    }

    @PostConstruct
    public void init() {
        /*
         * 自定义逻辑处理被限流的请求，默认实现为DefaultBlockRequestHandler
         */
        GatewayCallbackManager.setBlockHandler((exchange, ex) -> ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON).bodyValue(new FallbackProvider().fallbackResponse(ex).toString()));
    }
}
