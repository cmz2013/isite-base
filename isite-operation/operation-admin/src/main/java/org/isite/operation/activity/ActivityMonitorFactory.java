package org.isite.operation.activity;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.operation.support.enums.ActivityTheme;
import org.springframework.stereotype.Component;

/**
 * @Description 运营任务监视工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ActivityMonitorFactory extends AbstractFactory<ActivityMonitor, ActivityTheme, Integer> {
}
