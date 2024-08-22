package org.isite.operation.task;

import org.springframework.lang.Nullable;

import java.util.Date;

import static org.isite.commons.lang.data.Constants.COLON;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.DateUtils.PATTERN_DATETIME;
import static org.isite.commons.lang.utils.DateUtils.formatDate;
import static org.isite.commons.lang.utils.StringUtils.join;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class IdempotentKey {

    private IdempotentKey() {
    }

    /**
     * 任务记录幂等KEY的五元素：活动ID、任务ID、任务周期（0：代表无任务周期）、用户ID、任务编号
     */
    public static String toValue(int activityId, int taskId, @Nullable Date periodStartTime,
                                  long userId, long taskNumber) {
        return join(COLON, activityId, taskId, null != periodStartTime ?
                        formatDate(periodStartTime, PATTERN_DATETIME) : ZERO, userId, taskNumber);
    }
}
