package org.isite.imports;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
//@EnableFeignClients(basePackages = {"org.isite.fms.client"})
@ComponentScan(basePackages = {"org.isite.fms"})
public class ImportFms {
}
