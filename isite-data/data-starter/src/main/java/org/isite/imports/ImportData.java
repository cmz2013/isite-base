package org.isite.imports;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description 使用配置类扫描包路径
 * 1）使用@ServletComponentScan注解后，Servlet、Filter、Listener 可以直接通过@WebServlet、@WebFilter、@WebListener 注解自动注册
 * 2）jar包的@FeignClient定义的客户端，需要通过@EnableFeignClients(basePackages = {})注解开启扫描
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@ComponentScan(basePackages = {"org.isite.data"})
//@EnableFeignClients(basePackages = {"org.isite.data.client"})
@ServletComponentScan(basePackages = {"org.isite.data"})
public class ImportData {
}
