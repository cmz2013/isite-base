package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class SignScoreReward extends ScoreReward {

    @Comment("连续签到天数")
    private Integer continuousCount;
}
