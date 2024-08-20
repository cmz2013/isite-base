package org.isite.operation.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Update;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.TaskPeriod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TaskPutDto extends Dto<Integer> {
    /**
     * 活动标题
     */
    @NotBlank(groups = Update.class)
    private String title;
    /**
     * 任务类型
     */
    @NotNull(groups = Update.class)
    private TaskType taskType;
    /**
     * @see TaskPeriod
     * 任务约束条件：任务周期频率JSON
     */
    private String taskPeriod;
    /**
     * 任务属性
     */
    private String property;
    /**
     * 备注
     */
    private String remark;
}
