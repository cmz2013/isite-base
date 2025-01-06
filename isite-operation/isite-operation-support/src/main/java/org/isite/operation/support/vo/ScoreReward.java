package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;
import org.isite.operation.support.enums.ScoreType;

/**
 * @Description 积分奖励
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ScoreReward extends Reward {

    @Comment("积分类型")
    private ScoreType scoreType;

    @Comment("积分值")
    private int scoreValue;
}
