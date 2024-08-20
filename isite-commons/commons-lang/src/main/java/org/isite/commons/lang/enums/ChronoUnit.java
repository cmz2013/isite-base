package org.isite.commons.lang.enums;

import lombok.Getter;

import static org.isite.commons.lang.data.Constants.EIGHT;
import static org.isite.commons.lang.data.Constants.FIVE;
import static org.isite.commons.lang.data.Constants.FOUR;
import static org.isite.commons.lang.data.Constants.NINE;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.SEVEN;
import static org.isite.commons.lang.data.Constants.SIX;
import static org.isite.commons.lang.data.Constants.THREE;
import static org.isite.commons.lang.data.Constants.TWO;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public enum ChronoUnit implements Enumerable<Integer> {
    /**
     * 秒
     */
    SECOND(ONE, 1000L),
    /**
     * 分钟
     */
    MINUTE(TWO, 60000L),
    /**
     * 小时
     */
    HOUR(THREE, 3600000L),
    /**
     * 天
     */
    DAY(FOUR, 86400000L),
    /**
     * 周
     */
    WEEK(FIVE, 604800000L),
    /**
     * 月
     */
    MONTH(SIX, 2592000000L),
    /**
     * 季度
     */
    QUARTER(SEVEN, 7776000000L),
    /**
     * 半年
     */
    HALF_YEAR(EIGHT, 15552000000L),
    /**
     * 年
     */
    YEAR(NINE, 31104000000L);

    private final Integer code;
    /**
     * 单位时间毫秒数
     */
    @Getter
    private final long millis;

    ChronoUnit(Integer code, long millis) {
        this.code = code;
        this.millis = millis;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

}
