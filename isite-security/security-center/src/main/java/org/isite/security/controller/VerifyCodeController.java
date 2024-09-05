package org.isite.security.controller;

import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.code.VerifyCodeHandler;
import org.isite.security.code.VerifyCodeHandlerFactory;
import org.isite.security.data.dto.VerifyCodeDto;
import org.isite.security.data.dto.VerifyCodeGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.cloud.data.Result.success;
import static org.isite.security.data.constants.UrlConstants.API_GET_VERIFY_CODE;
import static org.isite.security.data.constants.UrlConstants.DELETE_VERIFY_CODE;

/**
 * @Description 验证码 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class VerifyCodeController extends BaseController {

    private VerifyCodeHandlerFactory verifyCodeHandlerFactory;

    /**
     * 主要用于前端不登录调用，不需要签名验证
     */
    @GetMapping(API_GET_VERIFY_CODE)
    public Result<Object> sendCode(@Validated VerifyCodeGetDto verifyCodeGetDto) {
        return toResult(() -> verifyCodeHandlerFactory.get(verifyCodeGetDto.getVerifyCodeMode()).sendCode(verifyCodeGetDto.getAgent()));
    }

    /**
     * 用于后端服务校验验证码，校验成功后删除验证码
     */
    @DeleteMapping(DELETE_VERIFY_CODE)
    public Result<?> checkCode(@Validated VerifyCodeDto verifyCodeDto) {
        VerifyCodeHandler verifyCodeHandler = verifyCodeHandlerFactory.get(verifyCodeDto.getVerifyCodeMode());
        isTrue(verifyCodeHandler.checkCode(verifyCodeDto.getAgent(), verifyCodeDto.getCode()),
                getMessage("VerifyCode.invalid", "the verification code is invalid"));
        return success();
    }

    @Autowired
    public void setVerifyCodeHandlerFactory(VerifyCodeHandlerFactory verifyCodeHandlerFactory) {
        this.verifyCodeHandlerFactory = verifyCodeHandlerFactory;
    }
}
