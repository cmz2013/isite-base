package org.isite.security.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.security.data.constants.SecurityConstants;
import org.isite.security.data.dto.CaptchaDto;
/**
 * @Description CaptchaClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class CaptchaAccessor {

    private CaptchaAccessor() {
    }

    /**
     * 校验验证码
     */
    public static boolean checkCaptcha(CaptchaDto captchaDto) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        CaptchaClient captchaClient = feignClientFactory.getFeignClient(CaptchaClient.class, SecurityConstants.SERVICE_ID);
        return ResultUtils.getData(captchaClient.checkCaptcha(captchaDto));
    }
}
