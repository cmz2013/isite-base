package org.isite.gateway.filter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.lang.schedule.RandomScheduler;
import org.isite.gateway.support.GatewayUtils;
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
import java.util.stream.Collectors;
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
        String serviceId = GatewayUtils.getServiceId(exchange);
        ServerHttpRequest request = exchange.getRequest();
        String version = request.getHeaders().getFirst(HttpHeaders.X_VERSION);
        if (StringUtils.isBlank(serviceId) || StringUtils.isBlank(version)) {
            return chain.filter(exchange);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        instances = instances.stream().filter(instance ->
                version.equals(instance.getMetadata().get(HttpHeaders.X_VERSION))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(instances)) {
            throw new NotFoundException("no " + serviceId + " found for version: " + version);
        }
        ServiceInstance instance = RandomScheduler.choose(instances);
        // 修改请求的URI
        request = request.mutate().uri(instance.getUri()).build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return 300;
    }
}
