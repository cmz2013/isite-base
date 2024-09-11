package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.misc.data.enums.ObjectType;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Description 运营任务对象约束条件
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "task_object")
public class TaskObjectPo extends Po<Long> {
    /**
     * 任务ID
     */
    private Integer taskId;
    /**
     * 任务约束条件：对象类型
     */
    private ObjectType objectType;
    /**
     * 任务约束条件：对象值
     */
    private String objectValue;
}
