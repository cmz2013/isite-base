package org.isite.data.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.data.support.constants.DataConstants;
import org.isite.data.support.enums.WsType;
import org.isite.data.support.vo.DataApi;

/**
 * @Description DataApiClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class DataApiAccessor {

    private DataApiAccessor() {
    }

    /**
     * 执行器调用数据接口
     */
    public static DataApi callApi(WsType wsType, String apiId, String signPassword) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        DataApiClient dataApiClient = feignClientFactory.getFeignClient(DataApiClient.class, DataConstants.SERVICE_ID);
        return ResultUtils.getData(dataApiClient.callApi(wsType, apiId, signPassword));
    }
}
