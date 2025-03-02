package org.isite.operation.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.lang.encoder.NumberEncoder.encode;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.support.constants.OperationUrls.URL_OPERATION;
import static org.isite.operation.support.enums.BusinessIdentity.OPERATION_ACTIVITY;
import static org.isite.operation.support.enums.InviteCodeType.USER_ID;

/**
 * @Description 邀请活动 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class InviteController extends BaseController {

    /**
     * 获取邀请码
     */
    @GetMapping(URL_MY + URL_OPERATION + "/invite/code")
    public Result<String> getInviteCode() {
        return toResult(encode(getUserId(), OPERATION_ACTIVITY.getCode(), USER_ID.getCode()));
    }
}
