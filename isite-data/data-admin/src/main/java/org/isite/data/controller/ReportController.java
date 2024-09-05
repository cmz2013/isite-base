package org.isite.data.controller;

import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.data.cache.ReportCache;
import org.isite.data.service.ReportService;
import org.isite.data.support.vo.ReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.data.converter.ReportConverter.toReportData;
import static org.isite.data.support.constants.CacheKey.DATA_CALL_FAILURE;
import static org.isite.data.support.constants.CacheKey.DATA_CALL_LATEST;
import static org.isite.data.support.constants.UrlConstants.URL_DATA;

/**
 * @Description 运行报表 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ReportController extends BaseController {

    private ReportCache reportCache;
    private ReportService reportService;

    @GetMapping(URL_DATA + "/report")
    public Result<ReportData> getReportData() {
        return toResult(toReportData(reportCache.countExecutor(), reportCache.countApi(),
                reportService.getCallDetail(DATA_CALL_LATEST),
                reportService.getCallDetail(DATA_CALL_FAILURE)));
    }

    @Autowired
    public void setReportCache(ReportCache reportCache) {
        this.reportCache = reportCache;
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
