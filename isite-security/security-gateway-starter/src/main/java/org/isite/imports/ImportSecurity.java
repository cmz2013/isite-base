package org.isite.imports;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description 使用配置类扫描包路径。
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@ComponentScan(basePackages = {"org.isite.security"})
@EnableFeignClients(basePackages = {"org.isite.security.gateway.client"})
public class ImportSecurity {
}
