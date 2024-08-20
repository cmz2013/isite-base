package org.isite.operation.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.operation.data.enums.BusinessIdentity;
import org.isite.operation.data.enums.InviteCodeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.constants.UrlConstants.URL_MY;
import static org.isite.commons.lang.encoder.NumberEncoder.encode;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.data.constants.UrlConstants.URL_OPERATION;

/**
 * @Description 邀请活动 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class InviteController extends BaseController {

    /**
     * 获取邀请码
     */
    @GetMapping(URL_MY + URL_OPERATION + "/invite/{identity}/code/{codeType}")
    public Result<String> getInviteCode(
            @PathVariable("identity") BusinessIdentity identity,
            @PathVariable("codeType") InviteCodeType codeType) {
        return toResult(encode(getUserId(), identity.getCode(), codeType.getCode()));
    }
}
