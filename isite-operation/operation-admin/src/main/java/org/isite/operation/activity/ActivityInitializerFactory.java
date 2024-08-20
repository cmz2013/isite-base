package org.isite.operation.activity;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.operation.data.enums.ActivityTheme;
import org.springframework.stereotype.Component;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ActivityInitializerFactory extends AbstractFactory<ActivityInitializer, ActivityTheme, Integer> {
}
