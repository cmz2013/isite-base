package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.code.CodeExecutor;
import org.isite.security.code.CodeExecutorFactory;
import org.isite.security.data.dto.VerificationCodeDto;
import org.isite.security.data.enums.VerificationCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.isite.commons.cloud.data.vo.Result.success;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.security.data.constants.UrlConstants.API_GET_VERIFICATION_CODE;
import static org.isite.security.data.constants.UrlConstants.DELETE_VERIFICATION_CODE;

/**
 * @Description 短信/邮件验证码 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class VerificationCodeController extends BaseController {

    private CodeExecutorFactory codeExecutorFactory;

    /**
     * 主要用于前端不登录调用，不需要签名验证
     */
    @Validated
    @GetMapping(API_GET_VERIFICATION_CODE)
    public Result<Object> sendCode(
            @NotNull @RequestParam("codeType") VerificationCodeType codeType,
            @NotBlank @RequestParam("agent") String agent) {
        return toResult(() -> codeExecutorFactory.get(codeType).sendCode(agent));
    }

    /**
     * 校验成功后删除验证码
     */
    @DeleteMapping(DELETE_VERIFICATION_CODE)
    public Result<?> checkCode(@Validated VerificationCodeDto verificationCodeDto) {
        CodeExecutor codeExecutor = codeExecutorFactory.get(verificationCodeDto.getCodeType());
        isTrue(codeExecutor.checkCode(verificationCodeDto.getAgent(), verificationCodeDto.getCode()),
                getMessage("verificationCode.invalid", "the verification code is invalid"));
        return success();
    }

    @Autowired
    public void setCodeExecutorFactory(CodeExecutorFactory codeExecutorFactory) {
        this.codeExecutorFactory = codeExecutorFactory;
    }
}
