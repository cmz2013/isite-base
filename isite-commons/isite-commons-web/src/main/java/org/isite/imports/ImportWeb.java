package org.isite.imports;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
/**
 * @Description 扫描包路径
 * 使用@ServletComponentScan注解后，Servlet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener 注解自动注册
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@ComponentScan(basePackages = {"org.isite.commons.web"})
@ServletComponentScan(basePackages = {"org.isite.commons.web"})
public class ImportWeb {
}
