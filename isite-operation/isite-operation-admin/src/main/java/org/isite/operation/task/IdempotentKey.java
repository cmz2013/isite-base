package org.isite.operation.task;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.DateUtils;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class IdempotentKey {

    private IdempotentKey() {
    }

    /**
     * 任务记录幂等KEY的五元素：活动ID、任务ID、任务周期（0：代表无任务周期）、用户ID、任务编号
     */
    public static String toValue(int activityId, int taskId, @Nullable LocalDateTime periodStartTime,
                                  long userId, long taskNumber) {
        return StringUtils.join(Constants.COLON, activityId, taskId, null != periodStartTime ?
                        DateUtils.format(periodStartTime, DateUtils.PATTERN_DATETIME) : Constants.ZERO, userId, taskNumber);
    }
}
