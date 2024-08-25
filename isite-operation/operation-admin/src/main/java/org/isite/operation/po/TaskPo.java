package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.operation.data.enums.EventType;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.TaskPeriod;
import org.isite.operation.data.vo.TaskProperty;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Description 运营任务
 * 运营任务是由业务行为（EventType）触发的，通过任务规则校验，判断是否可以完成运营任务，即是否可以获取抽奖次数、活动积分等
 * 任务规则包含EventType、ObjectType、objectId和规则参数（ruleParam）
 * 规则参数，是根据TaskType#EventType进行自定义
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "task")
public class TaskPo extends Po<Integer> {
    /**
     * 任务标题
     */
    private String title;
    /**
     * 运营活动id
     */
    private Integer activityId;
    /**
     * 触发任务的行为类型（和 Controller 接口一一对应）
     */
    private EventType eventType;
    /**
     * 任务类型
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private TaskType taskType;
    /**
     * @see TaskPeriod
     * 任务约束条件：任务周期频率JSON
     */
    private String taskPeriod;
    /**
     * @see TaskProperty
     * 任务约束条件：任务属性JSON
     */
    private String property;
    /**
     * 备注
     */
    private String remark;
}
