package org.isite.operation.client;

import org.isite.commons.cloud.data.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.isite.operation.support.constants.UrlConstants.PUT_VIP_SCORE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "vipScoreClient", value = SERVICE_ID)
public interface VipScoreClient {

    @PostMapping(PUT_VIP_SCORE)
    Result<Integer> deductVipScore(@RequestParam(("vipScore")) Integer vipScore);
}
