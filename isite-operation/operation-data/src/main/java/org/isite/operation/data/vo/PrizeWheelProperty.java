package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * @Description 抽奖活动属性
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeWheelProperty extends ActivityProperty {
    /**
     * 抽奖之前增送抽奖次数
     */
    @Comment("增送抽奖次数")
    private Boolean freeLottery;
    /**
     * 增送抽奖次数约束条件：按每个人的维度定义的任务周期频率
     */
    @Comment("赠送周期频率")
    private TaskPeriod taskPeriod;
}
