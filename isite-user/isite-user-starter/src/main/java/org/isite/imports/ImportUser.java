package org.isite.imports;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description 使用配置类扫描包路径
 * jar包的@FeignClient定义的客户端，需要通过@EnableFeignClients(basePackages = {})注解开启扫描
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
//@EnableFeignClients(basePackages = {"org.isite.user.client"})
@ComponentScan(basePackages = {"org.isite.user"})
public class ImportUser {
}
