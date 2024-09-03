package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * @Description 订单奖品任务属性
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class OrderPrizeProperty extends PrizeTaskProperty {

    @Comment("最近一年内累计消费金额(单位：元)")
    private Integer cumulateAmount;
}
