package org.isite.data.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.data.support.enums.WsType;
import org.isite.data.support.vo.DataApi;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.lang.utils.ResultUtils.getData;
import static org.isite.data.support.constants.DataConstants.SERVICE_ID;

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
        DataApiClient dataApiClient = getBean(FeignClientFactory.class).getFeignClient(DataApiClient.class, SERVICE_ID);
        return getData(dataApiClient.callApi(wsType, apiId, signPassword));
    }

}
