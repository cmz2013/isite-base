package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.code.CaptchaExecutor;
import org.isite.security.code.CaptchaExecutorFactory;
import org.isite.security.data.constants.SecurityUrls;
import org.isite.security.data.dto.CaptchaDto;
import org.isite.security.data.enums.CaptchaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * @Description 短信/邮件验证码 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class CaptchaController extends BaseController {
    private CaptchaExecutorFactory captchaExecutorFactory;

    /**
     * 主要用于前端不登录调用，不需要签名验证
     */
    @Validated
    @GetMapping(SecurityUrls.API_GET_CAPTCHA)
    public Result<Object> sendCaptcha(
            @NotNull @RequestParam("captchaType") CaptchaType captchaType,
            @NotBlank @RequestParam("agent") String agent) {
        return toResult(() -> captchaExecutorFactory.get(captchaType).sendCaptcha(agent));
    }

    /**
     * 校验成功后删除验证码
     */
    @DeleteMapping(SecurityUrls.DELETE_CAPTCHA)
    public Result<?> checkCaptcha(@Validated CaptchaDto captchaDto) {
        return toResult(() -> {
            CaptchaExecutor captchaExecutor = captchaExecutorFactory.get(captchaDto.getCaptchaType());
            Assert.isTrue(captchaExecutor.checkCaptcha(captchaDto.getAgent(), captchaDto.getCode()),
                    MessageUtils.getMessage("captcha.invalid", "the captcha is invalid"));
        });
    }

    @Autowired
    public void setCaptchaExecutorFactory(CaptchaExecutorFactory captchaExecutorFactory) {
        this.captchaExecutorFactory = captchaExecutorFactory;
    }
}
