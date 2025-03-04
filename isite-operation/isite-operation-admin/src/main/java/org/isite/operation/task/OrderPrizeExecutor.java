package org.isite.operation.task;

import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.OrderPrizeProperty;
import org.isite.operation.support.vo.TaskProperty;
import org.springframework.stereotype.Component;
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
        OrderPrizeProperty orderPrizeProperty = TypeUtils.cast(taskProperty);
        //用户一年内累计消费金额是否满足奖品奖励条件
        int cumulateAmount = TypeUtils.cast(eventDto.getEventParam());
        return cumulateAmount >= orderPrizeProperty.getCumulateAmount();
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {TaskType.SHOP_ORDER_PRIZE};
    }
}
