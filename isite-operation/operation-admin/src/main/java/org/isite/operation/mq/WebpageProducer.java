package org.isite.operation.mq;

import org.isite.commons.web.mq.Producer;
import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.InviteEventParam;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.commons.web.utils.RequestUtils.getRequest;
import static org.isite.operation.data.constants.OperationConstants.FIELD_INVITE;
import static org.isite.operation.data.enums.EventType.GET_OPERATION_WEBPAGE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WebpageProducer implements Producer {

    @Override
    public EventDto getBody(Object[] args, Object returnValue) {
        Long userId = getUserId();
        if (null == userId) {
            return null;
        }
        Activity activity = cast(args[ZERO]);
        EventDto eventDto = new EventDto();
        eventDto.setUserId(userId);
        eventDto.setEventType(GET_OPERATION_WEBPAGE);
        eventDto.setObjectValue(valueOf(activity.getId()));

        InviteEventParam eventParam = new InviteEventParam();
        //活动邀请码
        eventParam.setInviteCode(getRequest().getParameter(FIELD_INVITE));
        eventDto.setEventParam(eventParam);
        return eventDto;
    }
}
