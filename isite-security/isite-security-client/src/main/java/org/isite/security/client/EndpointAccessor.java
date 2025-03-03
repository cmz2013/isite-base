package org.isite.security.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.security.data.constants.SecurityConstants;
import org.isite.security.data.vo.OauthClient;

import java.util.List;

/**
 * @Description EndpointClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class EndpointAccessor {

    private EndpointAccessor() {
    }


    /**
     * 查询客户端名称
     */
    public static List<OauthClient> findOauthClients(List<String> clientIds) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        EndpointClient endpointClient = feignClientFactory.getFeignClient(EndpointClient.class, SecurityConstants.SERVICE_ID);
        return ResultUtils.getData(endpointClient.findOauthClients(clientIds));
    }
}
