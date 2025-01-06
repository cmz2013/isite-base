package org.isite.security.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.security.data.dto.VerificationCodeDto;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.ResultUtils.getData;
import static org.isite.security.data.constants.SecurityConstants.SERVICE_ID;

/**
 * @Description VerifyCodeClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class VerificationCodeAccessor {

    private VerificationCodeAccessor() {
    }

    /**
     * 校验验证码
     */
    public static boolean checkCode(VerificationCodeDto codeDto) {
        FeignClientFactory feignClientFactory = getBean(FeignClientFactory.class);
        VerificationCodeClient verificationCodeClient = feignClientFactory.getFeignClient(VerificationCodeClient.class, SERVICE_ID);
        return getData(verificationCodeClient.checkCode(codeDto));
    }
}
