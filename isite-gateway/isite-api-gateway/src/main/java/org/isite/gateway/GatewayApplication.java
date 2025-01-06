package org.isite.gateway;

import org.isite.imports.EnableCloud;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@EnableCloud
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        run(GatewayApplication.class, args);
    }
}
