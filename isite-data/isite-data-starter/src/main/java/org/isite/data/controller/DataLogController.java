package org.isite.data.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.data.log.DataLogService;
import org.isite.data.support.dto.DataLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.data.support.constants.DataUrls.PUT_LOG_RETRY;

/**
 * @Description 数据补偿API
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DataLogController extends BaseController {

    private DataLogService dataLogService;

    /**
     * @return 执行完数据补偿，返回日志信息，如果返回空即为清除日志
     */
    @PutMapping(PUT_LOG_RETRY)
    public Result<DataLogDto> retry(@RequestBody DataLogDto logDto) {
        return toResult(dataLogService.retry(logDto));
    }

    @Autowired
    public void setDataLogService(DataLogService dataLogService) {
        this.dataLogService = dataLogService;
    }
}
