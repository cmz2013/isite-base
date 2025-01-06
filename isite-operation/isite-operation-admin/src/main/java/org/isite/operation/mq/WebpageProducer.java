package org.isite.operation.mq;

import org.isite.commons.web.mq.Producer;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.vo.InviteEventParam;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.utils.ResultUtils.isOk;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.commons.web.utils.RequestUtils.getRequest;
import static org.isite.operation.support.constants.OperationConstants.FIELD_INVITE;
import static org.isite.operation.support.enums.EventType.GET_OPERATION_WEBPAGE;

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
        String inviteCode = getRequest().getParameter(FIELD_INVITE);
        if (isOk(cast(returnValue)) && isNotBlank(inviteCode)) {
            EventDto eventDto = new EventDto();
            eventDto.setUserId(userId);
            eventDto.setEventType(GET_OPERATION_WEBPAGE);
            eventDto.setObjectValue(valueOf(args[ZERO]));

            InviteEventParam eventParam = new InviteEventParam();
            //活动邀请码
            eventParam.setInviteCode(inviteCode);
            eventDto.setEventParam(eventParam);
            return eventDto;
        }
        return null;
    }
}
