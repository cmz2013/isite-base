package org.isite.operation.service;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.mq.EventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.isite.operation.data.enums.EventType.POST_SHOP_ORDER_PAYMENT_NOTIFY;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class UserFeedbackService {

    private EventConsumer eventConsumer;

    /**
     * 如果用户是首次参加当前进行中的活动，需要统计用户历史消费金额发放福利
     */
    public void grantPrize(Activity activity, long userId) {
        EventDto eventDto = new EventDto();
        eventDto.setUserId(userId);
        eventDto.setEventType(POST_SHOP_ORDER_PAYMENT_NOTIFY);
        eventConsumer.handle(activity, eventDto);
    }

    /**
     * 查询用户累计消费金额
     */
    public int findCumulateAmount(Long userId, Date startTime) {
        // TODO 调用商城服务查询用户累计消费金额
        return 1000;
    }

    @Autowired
    public void setEventConsumer(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }
}
