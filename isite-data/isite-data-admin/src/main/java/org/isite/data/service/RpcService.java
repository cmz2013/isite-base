package org.isite.data.service;

import org.isite.commons.lang.Assert;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.data.po.DataApiPo;
import org.isite.data.support.constants.CacheKeys;
import org.isite.data.support.enums.WsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class RpcService {
    private ReportService reportService;
    private DataApiService dataApiService;

    /**
     * 根据appCode和apiId调用数据接口
     */
    public DataApiPo callApi(String appCode, WsType wsType, String apiId) {
        DataApiPo apiPo = new DataApiPo();
        apiPo.setId(apiId);
        apiPo.setAppCode(appCode);
        apiPo.setWsType(wsType);
        apiPo.setStatus(ActiveStatus.ENABLED);

        apiPo = dataApiService.findOne(apiPo);
        Assert.notNull(apiPo, String.format("not found: appCode=%s, wsType=%s, apiId=%s", appCode, wsType, apiId));
        this.reportService.saveCallDetail(CacheKeys.DATA_CALL_LATEST);
        return apiPo;
    }

    @Autowired
    public void setDataApiService(DataApiService dataApiService) {
        this.dataApiService = dataApiService;
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
