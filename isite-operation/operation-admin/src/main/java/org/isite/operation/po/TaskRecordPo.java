package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.misc.data.enums.ObjectType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;

/**
 * @Description 任务参与记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TaskRecordPo extends Po<Long> {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 主活动ID
     */
    private Integer activityPid;
    /**
     * 任务ID
     */
    private Integer taskId;
    /**
     * 完成任务的数据对象类型
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ObjectType objectType;
    /**
     * 数据对象值（ID等）
     */
    private String objectValue;
    /**
     * 任务完成时间
     */
    private Date finishTime;
    /**
     * 幂等键：用于任务记录去重
     */
    private String idempotentKey;
    /**
     * 备注
     */
    private String remark;
}
