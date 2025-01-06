package org.isite.imports;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @Description 扫描包路径
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@ComponentScan(basePackages = {"org.isite.commons.cloud"})
@EnableAspectJAutoProxy
public class ImportCloud {
}
