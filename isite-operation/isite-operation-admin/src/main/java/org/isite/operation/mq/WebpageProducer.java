package org.isite.operation.mq;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.commons.web.mq.Producer;
import org.isite.commons.web.utils.RequestUtils;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.vo.InviteParam;
import org.springframework.stereotype.Component;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WebpageProducer implements Producer {

    @Override
    public EventDto getBody(Object[] args, Object returnValue) {
        Long userId = TransmittableHeaders.getUserId();
        if (null == userId) {
            return null;
        }
        String inviteCode = RequestUtils.getRequest().getParameter(OperationConstants.FIELD_INVITE);
        if (ResultUtils.isOk(TypeUtils.cast(returnValue)) && StringUtils.isNotBlank(inviteCode)) {
            EventDto eventDto = new EventDto();
            eventDto.setUserId(userId);
            eventDto.setEventType(EventType.GET_OPERATION_WEBPAGE);
            eventDto.setObjectValue(String.valueOf(args[Constants.ZERO]));
            InviteParam inviteParam = new InviteParam();
            //活动邀请码
            inviteParam.setInviteCode(inviteCode);
            eventDto.setEventParam(inviteParam);
            return eventDto;
        }
        return null;
    }
}
