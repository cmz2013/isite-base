package org.isite.data.client;

import org.isite.commons.cloud.data.Result;
import org.isite.data.support.dto.DataLogDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.isite.data.support.constants.UrlConstants.PUT_LOG_RETRY;

/**
 * @Description 数据补偿客户端
 * @Author <font color='blue'>zhangcm</font>
 */
public interface DataCompensateClient {
    /**
     * 执行完数据补偿，返回日志信息，如果返回空即为清除日志
     */
    @PostMapping(value = PUT_LOG_RETRY)
    Result<DataLogDto> retry( @RequestBody DataLogDto logDto);
}
