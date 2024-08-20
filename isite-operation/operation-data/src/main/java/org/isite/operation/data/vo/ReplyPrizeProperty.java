package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * 答疑奖品任务属性
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ReplyPrizeProperty extends PrizeTaskProperty {
    /**
     * 是否首答，空：不限制
     */
    @Comment("是否首答")
    private Boolean firstReply;
    /**
     * 最大响应时间（分钟），空：不限制
     */
    @Comment("最大响应时间（分钟）")
    private Integer maxResponseTime;
}
