package org.isite.data;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.springframework.boot.SpringApplication.run;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableMethodCache(basePackages = "org.isite.data")
@SpringBootApplication
public class DataApplication {

    public static void main(String[] args) {
        run(DataApplication.class, args);
    }
}
