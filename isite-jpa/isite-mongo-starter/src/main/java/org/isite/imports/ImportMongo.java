package org.isite.imports;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description 扫描mongo-starter包路径
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@ComponentScan(basePackages = {"org.isite.mongo"})
public class ImportMongo {
}
