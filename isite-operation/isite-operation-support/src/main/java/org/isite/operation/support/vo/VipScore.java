package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class VipScore {
    //可用的总积分
    private int totalScore;
    //快要过期的积分
    private int aboutToExpire;

    public VipScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public VipScore(int totalScore, int aboutToExpire) {
        this.totalScore = totalScore;
        this.aboutToExpire = aboutToExpire;
    }
}
