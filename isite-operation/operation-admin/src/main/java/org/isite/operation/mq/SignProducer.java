package org.isite.operation.mq;

import org.isite.commons.web.mq.Producer;
import org.isite.operation.po.SignLogPo;
import org.isite.operation.data.dto.EventDto;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.EventType.POST_SIGN;
/**
 * @author <font color='blue'>zhangcm</font>
 */
@Component
public class SignProducer implements Producer {

    /**
     * 每日签到行为消息
     */
    @Override
    public Object getBody(Object[] args, Object returnValue) {
        EventDto eventDto = new EventDto();
        SignLogPo signLogPo = cast(returnValue);
        eventDto.setUserId(signLogPo.getUserId());
        eventDto.setEventType(POST_SIGN);
        eventDto.setObjectValue(signLogPo.getId().toString());
        eventDto.setEventParam(signLogPo.getContinuousCount());
        return eventDto;
    }
}
