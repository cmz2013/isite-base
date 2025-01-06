package org.isite.data.log;

import org.isite.data.support.dto.DataLogDto;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ErrorStrategy implements LogStrategy {

    /**
     * 接口调用成功时，不保存日志信息；调用失败时，保存日志信息
     */
    @Override
    public boolean discard(DataLogDto logDto) {
        return logDto.getStatus();
    }
}
