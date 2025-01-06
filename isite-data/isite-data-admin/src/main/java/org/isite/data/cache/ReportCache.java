package org.isite.data.cache;

import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.isite.data.po.DataApiPo;
import org.isite.data.service.DataApiService;
import org.isite.data.service.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;
import static org.isite.data.support.constants.CacheKey.DATA_API_NUMBER;
import static org.isite.data.support.constants.CacheKey.DATA_CALL_PREFIX;
import static org.isite.data.support.constants.CacheKey.DATA_EXECUTOR_NUMBER;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ReportCache {
    private DataApiService dataApiService;
    private ExecutorService executorService;

    @Cached(name = DATA_EXECUTOR_NUMBER)
    public long countExecutor() {
        return executorService.countAll();
    }

    @Cached(name = DATA_API_NUMBER)
    public long countApi() {
        return dataApiService.count(DataApiPo::getStatus, ENABLED);
    }

    /**
     * name会被用于缓存key的前缀
     * 使用SpEL指定key和value
     */
    @CacheUpdate(name = DATA_CALL_PREFIX, key = "#key", value = "#callDetail")
    public void saveCallDetail(String key, Map<Long, Integer> callDetail) {
    }

    @Cached(name = DATA_CALL_PREFIX, key = "#key")
    public Map<Long, Integer> getCallDetail(String key) {
        return new HashMap<>(ONE);
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Autowired
    public void setDataApiService(DataApiService dataApiService) {
        this.dataApiService = dataApiService;
    }
}
