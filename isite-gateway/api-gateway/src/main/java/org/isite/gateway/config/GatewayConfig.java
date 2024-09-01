package org.isite.gateway.config;

import org.isite.gateway.exception.GlobalExceptionHandler;
import org.isite.gateway.filter.GrayLoadBalancer;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class GatewayConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public GrayLoadBalancer grayLoadBalancer(DiscoveryClient discoveryClient) {
        return new GrayLoadBalancer(discoveryClient);
    }
}
