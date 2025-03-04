package org.isite.commons.lang.enums;

import lombok.Getter;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public enum ChronoUnit implements Enumerable<String> {
    /**
     * 秒
     */
    SECOND(1000L),
    /**
     * 分钟
     */
    MINUTE(60000L),
    /**
     * 小时
     */
    HOUR(3600000L),
    /**
     * 天
     */
    DAY(86400000L),
    /**
     * 周
     */
    WEEK(604800000L),
    /**
     * 月
     */
    MONTH(2592000000L),
    /**
     * 季度
     */
    QUARTER(7776000000L),
    /**
     * 年
     */
    YEAR(31104000000L);

    /**
     * 单位时间毫秒数
     */
    private final long millis;

    ChronoUnit(long millis) {
        this.millis = millis;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
