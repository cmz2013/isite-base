package org.isite.operation.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.operation.data.dto.EventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.operation.data.constants.OperationConstants.QUEUE_OPERATION_EVENT;

/**
 * @Description 运营任务事件接收机制
 * 运营任务是由业务接口在处理完用户请求时，或由系统定时任务发送运营任务事件触发的
 * 运营任务事件接收机制，在任务规则校验通过后执行运营任务，发放抽奖次数、活动积分等
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = QUEUE_OPERATION_EVENT)
public class EventReceiver extends ReceiverWrapper<EventDto> {

    @Autowired
    public EventReceiver(EventConsumer consumer) {
        super(consumer);
    }
}
