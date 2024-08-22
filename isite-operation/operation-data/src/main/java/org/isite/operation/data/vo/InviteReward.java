package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * @Description 邀请奖励
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class InviteReward extends Reward {

    @Comment("奖品")
    private Integer prizeId;

}
