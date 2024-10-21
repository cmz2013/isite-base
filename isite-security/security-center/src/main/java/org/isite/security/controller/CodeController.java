package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.code.CodeHandler;
import org.isite.security.code.CodeHandlerFactory;
import org.isite.security.data.dto.VerifyCodeDto;
import org.isite.security.data.dto.VerifyCodeGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.cloud.data.vo.Result.success;
import static org.isite.security.data.constants.UrlConstants.API_GET_VERIFY_CODE;
import static org.isite.security.data.constants.UrlConstants.DELETE_VERIFY_CODE;

/**
 * @Description 验证码 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class CodeController extends BaseController {

    private CodeHandlerFactory codeHandlerFactory;

    /**
     * 主要用于前端不登录调用，不需要签名验证
     */
    @GetMapping(API_GET_VERIFY_CODE)
    public Result<Object> sendCode(@Validated VerifyCodeGetDto verifyCodeGetDto) {
        return toResult(() -> codeHandlerFactory.get(verifyCodeGetDto.getCodeMode()).sendCode(verifyCodeGetDto.getAgent()));
    }

    /**
     * 用于后端服务校验验证码，校验成功后删除验证码
     */
    @DeleteMapping(DELETE_VERIFY_CODE)
    public Result<?> checkCode(@Validated VerifyCodeDto verifyCodeDto) {
        CodeHandler codeHandler = codeHandlerFactory.get(verifyCodeDto.getCodeMode());
        isTrue(codeHandler.checkCode(verifyCodeDto.getAgent(), verifyCodeDto.getCode()),
                getMessage("code.invalid", "the verification code is invalid"));
        return success();
    }

    @Autowired
    public void setCodeHandlerFactory(CodeHandlerFactory codeHandlerFactory) {
        this.codeHandlerFactory = codeHandlerFactory;
    }
}
