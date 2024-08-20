package org.isite.data.log;

import lombok.SneakyThrows;
import org.isite.data.callback.DataCallback;
import org.isite.data.handler.DataHandler;
import org.isite.data.support.dto.DataLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Class.forName;
import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DataLogService {

    private LogStrategy logStrategy;

    @Autowired
    public void setLogStrategy(LogStrategy logStrategy) {
        this.logStrategy = logStrategy;
    }

    @SneakyThrows
    public DataLogDto retry(DataLogDto logDto) {
        Object executor = getBean(forName(logDto.getApiClass()));
        logDto = executor instanceof DataHandler<?, ?> ?
                ((DataHandler<?, ?>) executor).handle(logDto) :
                ((DataCallback<?, ?>) executor).execute(logDto);
        return logStrategy.discard(logDto) ? null : logDto;
    }
}
