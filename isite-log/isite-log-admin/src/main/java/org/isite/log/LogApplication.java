package org.isite.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.springframework.boot.SpringApplication.run;

/**
 * @Description 如果没有使用@MapperScan注解，就需要在接口上增加@Mapper注解，否则MyBatis无法判断扫描哪些接口。
 * @Author <font color='blue'>zhangcm</font>
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LogApplication {

    public static void main(String[] args) {
        run(LogApplication.class, args);
    }
}
