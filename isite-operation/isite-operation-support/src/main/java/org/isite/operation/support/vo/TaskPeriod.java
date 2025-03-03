package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.utils.DateUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

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
    public LocalDateTime getStartTime() {
        if (null == this.unit || null == this.duration) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.now().minus(
                this.unit.getMillis() * (this.duration - Constants.ONE), MILLIS);
        switch (unit) {
            case HOUR: {
                return DateUtils.startOfHour(dateTime);
            }
            case DAY: {
                return DateUtils.startOfDay(dateTime);
            }
            case MONTH: {
                return DateUtils.startOfMonth(dateTime);
            }
            case WEEK: {
                return DateUtils.startOfWeek(dateTime);
            }
            case QUARTER: {
                return DateUtils.startOfQuarter(dateTime);
            }
            case YEAR: {
                return DateUtils.startOfYear(dateTime);
            }
            default: return null;
        }
    }
}
