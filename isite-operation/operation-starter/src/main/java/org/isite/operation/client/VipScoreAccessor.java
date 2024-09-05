package org.isite.operation.client;

import org.isite.commons.web.feign.FeignClientFactory;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.ResultUtils.getData;
import static org.isite.operation.support.constants.OperationConstants.SERVICE_ID;

/**
 * @Description UserClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class VipScoreAccessor {

    private VipScoreAccessor() {
    }

    /**
     * @Description 抵扣VIP积分，返回剩余可用积分
     */
    public static Integer deductVipScore(Integer vipScore) {
        FeignClientFactory feignClientFactory = getBean(FeignClientFactory.class);
        VipScoreClient vipScoreClient = feignClientFactory.getFeignClient(VipScoreClient.class, SERVICE_ID);
        return getData(vipScoreClient.deductVipScore(vipScore));
    }
}
