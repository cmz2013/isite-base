package org.isite.security.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.security.data.dto.VerifyCodeDto;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.ResultUtils.getData;
import static org.isite.security.data.constants.SecurityConstants.SERVICE_ID;

/**
 * @Description VerifyCodeClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class VerifyCodeAccessor {

    private VerifyCodeAccessor() {
    }

    /**
     * 校验验证码
     */
    public static boolean checkCode(VerifyCodeDto verifyCodeDto) {
        FeignClientFactory feignClientFactory = getBean(FeignClientFactory.class);
        VerifyCodeClient verifyCodeClient = feignClientFactory.getFeignClient(VerifyCodeClient.class, SERVICE_ID);
        return getData(verifyCodeClient.checkCode(verifyCodeDto));
    }
}
