package org.isite.operation.client;

import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.feign.FeignClientFactory;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.operation.support.constants.OperationConstants.SERVICE_ID;

/**
 * @Description UserClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class VipScoreAccessor {

    private VipScoreAccessor() {
    }

    /**
     * @Description 使用VIP积分
     */
    public static Result<?> useVipScore(Integer score) {
        FeignClientFactory feignClientFactory = getBean(FeignClientFactory.class);
        VipScoreClient vipScoreClient = feignClientFactory.getFeignClient(VipScoreClient.class, SERVICE_ID);
        return vipScoreClient.useVipScore(score);
    }
}
