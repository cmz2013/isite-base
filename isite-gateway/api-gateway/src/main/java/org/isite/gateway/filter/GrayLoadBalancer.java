package org.isite.gateway.filter;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.isite.commons.cloud.constants.HttpHeaders.X_VERSION;
import static org.isite.commons.lang.schedule.RandomScheduler.choose;
import static org.isite.gateway.support.GatewayUtils.getServiceId;

/**
 * @Description 根据请求头服务版本号来决定路由到哪个服务
 * @Author <font color='blue'>zhangcm</font>
 */
public class GrayLoadBalancer implements GlobalFilter, Ordered {

    private final DiscoveryClient discoveryClient;

    public GrayLoadBalancer(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String serviceId = getServiceId(exchange);
        ServerHttpRequest request = exchange.getRequest();
        String version = request.getHeaders().getFirst(X_VERSION);
        if (isBlank(serviceId) || isBlank(version)) {
            return chain.filter(exchange);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        instances = instances.stream().filter(
                instance -> version.equals(instance.getMetadata().get(X_VERSION))).collect(toList());
        if (isEmpty(instances)) {
            throw new NotFoundException("no " + serviceId + " found for version: " + version);
        }
        ServiceInstance instance = choose(instances);
        // 修改请求的URI
        request = request.mutate()
                .uri(instance.getUri()).build();
        return chain.filter(exchange.mutate()
                .request(request).build());
    }

    @Override
    public int getOrder() {
        return 300;
    }
}
