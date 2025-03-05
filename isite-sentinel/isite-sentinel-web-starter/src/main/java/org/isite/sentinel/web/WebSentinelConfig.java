package org.isite.sentinel.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class WebSentinelConfig {

    @Bean
    public BlockRequestHandler blockRequestHandler() {
        return new BlockRequestHandler();
    }
}