package org.isite.operation.data.vo;

import org.isite.commons.lang.json.Comment;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class SignScoreReward extends ScoreReward {

    @Override
    @Comment("连续签到天数")
    public Integer getCoefficient() {
        return super.getCoefficient();
    }
}
