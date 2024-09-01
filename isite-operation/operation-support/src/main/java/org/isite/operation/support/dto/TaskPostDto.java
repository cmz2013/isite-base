package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.TaskPeriod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TaskPostDto {
    /**
     * 活动标题
     */
    @NotBlank
    private String title;
    /**
     * 运营活动id
     */
    @NotNull
    private Integer activityId;
    /**
     * 任务类型
     */
    @NotNull
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
