package org.isite.imports;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description 扫描mybatis-starter包路径
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@ComponentScan(basePackages = {"org.isite.mybatis"})
public class ImportMybatis {
}
