package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;
import org.isite.operation.data.enums.ScoreType;

/**
 * 运营任务奖励：积分
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ScoreReward extends Reward {

    @Comment("积分类型")
    private ScoreType scoreType;

    @Comment("积分值")
    private Integer scoreValue;
}
