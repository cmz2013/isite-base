package org.isite.operation.activity;

import org.isite.commons.lang.Constants;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.support.enums.ActivityTheme;
import org.isite.operation.support.vo.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WishPrizeMonitor implements ActivityMonitor {
    private PrizeRecordService prizeRecordService;

    /**
     * 用户参与活动任务须要先完成许愿
     */
    @Override
    public boolean doTask(Activity activity, long userId) {
        return prizeRecordService.count(activity.getId(), userId, null) > Constants.ZERO;
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Override
    public ActivityTheme[] getIdentities() {
        return new ActivityTheme[] {ActivityTheme.WISH_PRIZE};
    }
}
