package org.isite.security.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.security.data.vo.OauthClient;

import java.util.List;
import java.util.Set;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.lang.utils.ResultUtils.getData;
import static org.isite.security.data.constants.SecurityConstants.SERVICE_ID;

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
    public static List<OauthClient> findOauthClients(Set<String> clientIds) {
        FeignClientFactory feignClientFactory = getBean(FeignClientFactory.class);
        EndpointClient endpointClient = feignClientFactory.getFeignClient(EndpointClient.class, SERVICE_ID);
        return getData(endpointClient.findOauthClients(clientIds));
    }
}
