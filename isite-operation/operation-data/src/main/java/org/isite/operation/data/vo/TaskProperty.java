package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 运营任务属性，通常用于配置简单属性，主要包括：
 * 1）任务其他约束条件
 * 2）完成任务发放的奖励（保存到任务记录数据表中）
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TaskProperty<T extends Reward> implements Serializable {
    /**
     * 运营任务奖励列表
     */
    @Comment("任务奖励")
    private List<T> rewards;
}
