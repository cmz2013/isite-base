package org.isite.data.cache;

import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.data.po.DataApiPo;
import org.isite.data.service.DataApiService;
import org.isite.data.service.ExecutorService;
import org.isite.data.support.constants.CacheKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ReportCache {
    private DataApiService dataApiService;
    private ExecutorService executorService;

    @Cached(name = CacheKeys.DATA_EXECUTOR_NUMBER)
    public long countExecutor() {
        return executorService.countAll();
    }

    @Cached(name = CacheKeys.DATA_API_NUMBER)
    public long countApi() {
        return dataApiService.count(DataApiPo::getStatus, ActiveStatus.ENABLED);
    }

    /**
     * name会被用于缓存key的前缀
     * 使用SpEL指定key和value
     */
    @CacheUpdate(name = CacheKeys.DATA_CALL_PREFIX, key = "#key", value = "#callDetail")
    public void saveCallDetail(String key, Map<Long, Integer> callDetail) {
    }

    @Cached(name = CacheKeys.DATA_CALL_PREFIX, key = "#key")
    public Map<Long, Integer> getCallDetail(String key) {
        return new HashMap<>(Constants.ONE);
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
