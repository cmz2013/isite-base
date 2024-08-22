package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.operation.data.enums.TaskType;

/**
 * @Description 运营任务。任务约束条件包含ObjectType、objectValue、taskPeriod和自定属性（Property）
 * 其他约束参数，是根据TaskType#EventType进行自定义
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Task extends Vo<Integer> {
    /**
     * 任务标题
     */
    private String title;
    /**
     * 任务类型
     */
    private TaskType taskType;
    /**
     * 按人定义的任务约束条件：任务周期频率
     */
    private TaskPeriod taskPeriod;
    /**
     * 任务属性
     */
    private TaskProperty<? extends Reward> property;
    /**
     * 备注
     */
    private String remark;
}
