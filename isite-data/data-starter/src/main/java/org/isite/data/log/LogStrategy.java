package org.isite.data.log;

import org.isite.data.support.dto.DataLogDto;

/**
 * @Description 数据接口日志存储策略
 * @Author <font color='blue'>zhangcm</font>
 */
public interface LogStrategy {

    /**
     * 是否丢弃日志
     * @param logDto 数据接口日志
     */
    boolean discard(DataLogDto logDto);
}
