package org.isite.data.service;

import org.isite.data.po.DataApiPo;
import org.isite.data.support.enums.WsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;
import static org.isite.data.support.constants.CacheKey.DATA_CALL_LATEST;

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
        apiPo.setAppCode(appCode);
        apiPo.setWsType(wsType);
        apiPo.setId(apiId);
        apiPo.setStatus(ENABLED);

        apiPo = dataApiService.findOne(apiPo);
        notNull(apiPo, format("not found: appCode=%s, wsType=%s, apiId=%s", appCode, wsType, apiId));
        this.reportService.saveCallDetail(DATA_CALL_LATEST);
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
