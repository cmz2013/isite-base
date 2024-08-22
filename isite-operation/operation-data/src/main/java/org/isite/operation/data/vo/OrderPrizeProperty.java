package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

import java.util.Date;

/**
 * 订单奖品任务属性
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class OrderPrizeProperty extends PrizeTaskProperty {

    @Comment("开始时间")
    private Date startTime;

    @Comment("累计消费金额")
    private Integer cumulateAmount;
}
