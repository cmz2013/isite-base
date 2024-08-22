package org.isite.operation.task;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.OrderPrizeProperty;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.TaskProperty;
import org.isite.operation.service.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.SHOP_ORDER_PRIZE;

/**
 * @Description 订单奖品任务接口，奖品奖励系数（coefficient）配置用户最低累计消费金额
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class OrderPrizeExecutor extends PrizeTaskExecutor {

    private UserFeedbackService userFeedbackService;

    /**
     * 校验用户最低消费金额
     */
    @Override
    protected boolean checkTaskProperty(TaskProperty<? extends Reward> taskProperty, EventDto eventDto) {
        OrderPrizeProperty orderPrizeProperty = cast(taskProperty);
        int cumulateAmount = userFeedbackService.findCumulateAmount(eventDto.getUserId(), orderPrizeProperty.getStartTime());
        return cumulateAmount >= orderPrizeProperty.getCumulateAmount();
    }

    @Autowired
    public void setUserFeedbackService(UserFeedbackService userFeedbackService) {
        this.userFeedbackService = userFeedbackService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {SHOP_ORDER_PRIZE};
    }
}
