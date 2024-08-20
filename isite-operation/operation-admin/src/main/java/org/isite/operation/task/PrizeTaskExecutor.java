package org.isite.operation.task;

import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.service.PrizeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.INQUIRY_PRIZE;

/**
 * @Description 奖品任务父接口，在领奖记录表 prize_record 中，保存用户的奖品记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PrizeTaskExecutor extends TaskExecutor<PrizeRecordPo> {

    private PrizeTaskService prizeTaskService;
    private PrizeRecordService prizeRecordService;

    @Override
    public void saveTaskRecord(Activity activity, PrizeRecordPo taskRecord, Reward reward) {
        prizeTaskService.saveTaskRecord(activity, taskRecord, cast(reward));
    }

    @Override
    protected long countTaskRecord(int activityId, int taskId, Date startTime, long userId) {
        return prizeRecordService.count(activityId, taskId, startTime, userId);
    }

    @Autowired
    public void setPrizeTaskService(PrizeTaskService prizeTaskService) {
        this.prizeTaskService = prizeTaskService;
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {INQUIRY_PRIZE};
    }
}
