package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.ChronoUnit;

import java.io.Serializable;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfDay;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfHalfYear;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfHour;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfMinute;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfMonth;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfQuarter;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfSecond;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfWeek;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfYear;

/**
 * @Description 任务周期频率，例如：3天6次（unit=DAYS time=3 limit=6）
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TaskPeriod implements Serializable {
    /**
     * 时间，和unit配合使用
     */
    private Long duration;
    /**
     * 时间单位
     */
    private ChronoUnit unit;
    /**
     * 可以重复参与任务的次数。
     * 1）如果time不为空，则表示在该时间内可以执行的最多次数
     * 2）如果time为空，则表示该任务在活动时间内可以执行的最多次数
     * 3）如果limit空，则表示该任务不限制执行次数
     */
    private Integer limit;

    /**
     * 获取当前任务周期的开始时间
     */
    public Date getStartTime() {
        if (null == this.unit || null == this.duration) {
            return null;
        }
        Date date = new Date(currentTimeMillis() - this.unit.getMillis() * (this.duration - ONE));
        switch (unit) {
            case HOUR: {
                return getStartTimeOfHour(date);
            }
            case DAY: {
                return getStartTimeOfDay(date);
            }
            case MONTH: {
                return getStartTimeOfMonth(date);
            }
            case WEEK: {
                return getStartTimeOfWeek(date);
            }
            case HALF_YEAR: {
                return getStartTimeOfHalfYear(date);
            }
            case QUARTER: {
                return getStartTimeOfQuarter(date);
            }
            case YEAR: {
                return getStartTimeOfYear(date);
            }
            case MINUTE: {
                return getStartTimeOfMinute(date);
            }
            case SECOND: {
                return getStartTimeOfSecond(date);
            }
            default: return null;
        }
    }
}
