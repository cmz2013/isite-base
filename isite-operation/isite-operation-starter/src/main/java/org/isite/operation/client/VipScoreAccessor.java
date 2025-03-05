package org.isite.operation.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.operation.support.constants.OperationConstants;

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
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        VipScoreClient vipScoreClient = feignClientFactory.getFeignClient(VipScoreClient.class, OperationConstants.SERVICE_ID);
        return vipScoreClient.useVipScore(score);
    }
}
