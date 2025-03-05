package org.isite.fms;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;
/**
 * @Description 如果没有使用@MapperScan注解，就需要在接口上增加 @Mapper 注解，否则 MyBatis 无法判断扫描哪些接口。
 * @Author <font color='blue'>zhangcm</font>
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableMethodCache(basePackages = "org.isite.fms")
@EnableTransactionManagement
@MapperScan(basePackages = {"org.isite.fms.mapper"})
@SpringBootApplication
public class FmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmsApplication.class, args);
    }
}
