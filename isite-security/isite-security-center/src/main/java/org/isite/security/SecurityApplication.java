package org.isite.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        run(SecurityApplication.class, args);
    }
}
