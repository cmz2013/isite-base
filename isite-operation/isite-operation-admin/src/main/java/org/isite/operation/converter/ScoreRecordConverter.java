package org.isite.operation.converter;

import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.support.enums.ScoreType;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ScoreRecordConverter {

    private ScoreRecordConverter() {
    }

    public static ScoreRecordPo toScoreRecordSelectivePo(int activityId, long userId, ScoreType scoreType) {
        ScoreRecordPo scoreRecordPo = new ScoreRecordPo();
        scoreRecordPo.setActivityId(activityId);
        scoreRecordPo.setUserId(userId);
        scoreRecordPo.setScoreType(scoreType);
        return scoreRecordPo;
    }
}
