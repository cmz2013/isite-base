package org.isite.operation.mq;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.mq.Producer;
import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.vo.SignLog;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;
import static org.isite.commons.lang.utils.ResultUtils.isOk;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.EventType.POST_OPERATION_SIGN;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SignProducer implements Producer {

    /**
     * 每日签到行为消息
     */
    @Override
    public Object getBody(Object[] args, Object returnValue) {
        Result<SignLog> result = cast(returnValue);
        if (isOk(result)) {
            EventDto eventDto = new EventDto();
            SignLog signLog = result.getData();
            eventDto.setUserId(signLog.getUserId());
            eventDto.setEventType(POST_OPERATION_SIGN);
            eventDto.setObjectValue(valueOf(signLog.getId()));
            eventDto.setEventParam(signLog.getContinuousCount());
            return eventDto;
        }
        return null;
    }
}
