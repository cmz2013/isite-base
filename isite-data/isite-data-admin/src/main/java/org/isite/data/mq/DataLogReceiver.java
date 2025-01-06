package org.isite.data.mq;

import org.isite.commons.web.mq.ReceiverWrapper;
import org.isite.data.support.dto.DataLogDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.data.support.constants.DataConstants.QUEUE_DATA_LOG;

/**
 * @Description 数据接口日志接收器
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@RabbitListener(queues = QUEUE_DATA_LOG)
public class DataLogReceiver extends ReceiverWrapper<DataLogDto> {

    @Autowired
    public DataLogReceiver(DataLogConsumer consumer) {
        super(consumer);
    }
}
