package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.utils.DateUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 时间单位。任务周期仅支持按天、小时、月、周、季度、年进行周期性执行，其他单位暂不支持
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
        switch (unit) {
            case HOUR: {
                return DateUtils.startOfHour(LocalDateTime.now().minusHours(this.duration - Constants.ONE));
            }
            case DAY: {
                return DateUtils.startOfDay(LocalDateTime.now().minusDays(this.duration - Constants.ONE));
            }
            case MONTH: {
                return DateUtils.startOfMonth(LocalDateTime.now().minusMonths(this.duration - Constants.ONE));
            }
            case WEEK: {
                return DateUtils.startOfWeek(LocalDateTime.now().minusWeeks(this.duration - Constants.ONE));
            }
            case QUARTER: {
                return DateUtils.startOfMonth(LocalDateTime.now().minusMonths(this.duration * Constants.THREE - Constants.ONE));
            }
            case YEAR: {
                return DateUtils.startOfYear(LocalDateTime.now().minusYears(this.duration - Constants.ONE));
            }
            default: return null;
        }
    }
}
