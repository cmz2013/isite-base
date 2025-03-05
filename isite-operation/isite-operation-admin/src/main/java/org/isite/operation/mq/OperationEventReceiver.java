package org.isite.operation.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.operation.support.dto.EventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @Description 使用 @RabbitListener 注解来监听运营任务事件消息队列。
 * 运营任务是由业务接口在处理完用户请求时，或由系统定时任务发送运营任务事件触发的。
 * 运营任务事件接收机制，在任务规则校验通过后执行运营任务，发放抽奖次数、活动积分等
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = OperationConstants.QUEUE_OPERATION_EVENT)
public class OperationEventReceiver extends ReceiverWrapper<EventDto> {

    @Autowired
    public OperationEventReceiver(OperationEventConsumer consumer) {
        super(consumer);
    }
}
