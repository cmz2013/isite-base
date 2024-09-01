package org.isite.operation.task;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.operation.support.enums.TaskType;
import org.springframework.stereotype.Component;

/**
 * @Description 任务执行接口工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class TaskExecutorFactory extends AbstractFactory<TaskExecutor<?>, TaskType, Integer> {
}
