package org.isite.gateway.support;

import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class GatewayUtils {

    private GatewayUtils() {
    }

    /**
     * @Description 获取服务ID。Gateway的数据封装在ServerWebExchange里.
     * 在Gateway路由uri配置中，lb://前缀用于表示负载均衡（Load Balancing），并且后面的部分是服务ID（serviceId）。
     */
    public static String getServiceId(ServerWebExchange exchange) {
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        if (routeUri != null && "lb".equals(routeUri.getScheme())) {
            return routeUri.getHost();
        }
        return null;
    }

}
