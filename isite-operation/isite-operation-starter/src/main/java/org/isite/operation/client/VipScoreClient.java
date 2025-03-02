package org.isite.operation.client;

import org.isite.commons.cloud.data.vo.Result;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.isite.operation.support.constants.OperationUrls.PUT_USE_VIP_SCORE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "vipScoreClient", value = SERVICE_ID)
public interface VipScoreClient {

    @PutMapping(PUT_USE_VIP_SCORE)
    Result<?> useVipScore(@RequestParam(("score")) Integer score);
}
