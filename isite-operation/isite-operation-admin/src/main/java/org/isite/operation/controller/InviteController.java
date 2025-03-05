package org.isite.operation.controller;

import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.encoder.NumberEncoder;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.enums.BusinessIdentity;
import org.isite.operation.support.enums.InviteCodeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 邀请活动 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class InviteController extends BaseController {
    /**
     * 获取邀请码
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/invite/code")
    public Result<String> getInviteCode() {
        return toResult(NumberEncoder.encode(TransmittableHeaders.getUserId(),
                BusinessIdentity.OPERATION_ACTIVITY.getCode(), InviteCodeType.USER_ID.getCode()));
    }
}
