package org.isite.data.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.data.po.DataLogPo;
import org.isite.data.service.DataLogService;
import org.isite.data.support.dto.DataLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @Description 保存数据日志
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class DataLogConsumer implements Consumer<DataLogDto> {

    private DataLogService dataLogService;

    @Autowired
    public void setDataLogService(DataLogService dataLogService) {
        this.dataLogService = dataLogService;
    }

    @Override
    public Basic handle(DataLogDto dataLogDto) {
        try {
            dataLogService.addDataLog(DataConverter.convert(dataLogDto, DataLogPo::new));
            return new Basic.Ack();
        } catch (Exception e) {
            log.error("save data log error", e);
            return new Basic.Nack();
        }
    }
}
