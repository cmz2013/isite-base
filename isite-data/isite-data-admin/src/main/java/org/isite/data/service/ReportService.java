package org.isite.data.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.DateUtils;
import org.isite.data.cache.ReportCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ReportService {

    private static final String DATA_CALL_KEY_PATTERN = "yyyyMMddHHmm";
    private ReportCache reportCache;

    /**
     * @Description 更新最近1分钟的调用次数
     * redis的多线程部分只是用来处理网络数据的读写和协议解析（网络I/O操作慢），执行命令任然是单线程顺序执行（内存操作快）。
     * 所以，我们不需要去考虑并发及线程安全问题
     */
    @Async
    public void saveCallDetail(String key) {
        Map<Long, Integer> callDetail = getCallDetail(key);
        long currTime = Long.parseLong(DateUtils.format(LocalDateTime.now(), DATA_CALL_KEY_PATTERN));
        callDetail.put(currTime, callDetail.getOrDefault(currTime, Constants.ZERO) + Constants.ONE);
        reportCache.saveCallDetail(key, callDetail);
    }

    /**
     * 删除接口调用1小时之前的数据
     */
    private void removeExpired(Map<Long, Integer> callDetail) {
        long minTime = Long.parseLong(DateUtils.format(
                LocalDateTime.now().minusHours(Constants.ONE), DATA_CALL_KEY_PATTERN));
        if (MapUtils.isNotEmpty(callDetail)) {
            List<Long> expiredTimes = new ArrayList<>();
            callDetail.keySet().forEach(time -> {
                if (time < minTime) {
                    expiredTimes.add(time);
                }
            });
            if (CollectionUtils.isNotEmpty(expiredTimes)) {
                expiredTimes.forEach(callDetail::remove);
            }
        }
    }

    /**
     * 获取接口最近1小时调用详情（每分钟的调用次数），删除过期数据
     */
    public Map<Long, Integer> getCallDetail(String key) {
        Map<Long, Integer> callDetail = reportCache.getCallDetail(key);
        removeExpired(callDetail);
        return callDetail;
    }

    @Autowired
    public void setReportCache(ReportCache reportCache) {
        this.reportCache = reportCache;
    }
}
