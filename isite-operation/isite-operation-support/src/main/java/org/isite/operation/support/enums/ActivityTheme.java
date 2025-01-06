package org.isite.operation.support.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.operation.support.vo.ActivityProperty;
import org.isite.operation.support.vo.PrizeWheelProperty;
import org.isite.operation.support.vo.WishPrizeProperty;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;

/**
 * 活动主题
 * @author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum ActivityTheme implements Enumerable<Integer> {
    /**
     * 好运星许愿活动，积分兑换奖品
     */
    WISH_PRIZE(1, "好运星许愿", null, WishPrizeProperty.class),
    PRIZE_WHEEL(101, "抽奖转盘", "prize_wheel", PrizeWheelProperty.class),
    /**
     * 老用户回馈（订单福利奖品活动）
     */
    USER_FEEDBACK(901, "老用户回馈", "user_feedback", null),
    /**
     * 用于配置其他活动信息
     */
    NONE(999, "NONE", null, null);

    private final Integer code;
    private final String label;
    /**
     * 活动网页默认模板文件名
     */
    private final String webpage;
    private final Class<? extends ActivityProperty> propertyClass;

    ActivityTheme(Integer code, String label, String webpage, Class<? extends ActivityProperty> propertyClass) {
        this.code = code;
        this.label = label;
        this.webpage = webpage;
        this.propertyClass = propertyClass;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public String getLabel() {
        return label;
    }

    @JsonIgnore
    public String getWebpage() {
        return webpage;
    }

    @JsonIgnore
    public Class<? extends ActivityProperty> getPropertyClass() {
        return propertyClass;
    }
}
