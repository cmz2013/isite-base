package org.isite.operation.task;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.OrderPrizeProperty;
import org.isite.operation.data.vo.TaskProperty;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.SHOP_ORDER_PRIZE;

/**
 * @Description 订单奖品任务接口，奖品奖励系数（coefficient）配置用户最低累计消费金额
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class OrderPrizeExecutor extends PrizeTaskExecutor {
    /**
     * 校验用户最低消费金额
     */
    @Override
    protected boolean checkTaskProperty(TaskProperty<?> taskProperty, EventDto eventDto) {
        OrderPrizeProperty orderPrizeProperty = cast(taskProperty);
        //用户一年内累计消费金额是否满足奖品奖励条件
        int cumulateAmount = cast(eventDto.getEventParam());
        return cumulateAmount >= orderPrizeProperty.getCumulateAmount();
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {SHOP_ORDER_PRIZE};
    }
}
