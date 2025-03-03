package org.isite.data.service;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.data.client.DataCompensateClient;
import org.isite.data.po.DataLogPo;
import org.isite.data.support.constants.CacheKeys;
import org.isite.data.support.dto.DataLogDto;
import org.isite.mongo.service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 接口日志Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DataLogService extends PoService<DataLogPo, String> {
    /**
     * 运行报表Service
     */
    private ReportService reportService;
    /**
     * 执行器客户端工厂
     */
    private FeignClientFactory feignClientFactory;
    private EmailAlerter emailAlerter;

    /**
     * @Description 执行完数据补偿，返回日志信息，如果返回空即为清除日志
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_DATA_LOG, keys = "#dataLogPo.id"))
    public DataLogDto retry(DataLogPo dataLogPo) {
        //根据DataLog中的appCode设置FeignClient的name（value）
        DataCompensateClient dataCompensateClient = feignClientFactory.getFeignClient(DataCompensateClient.class, dataLogPo.getAppCode());
        DataLogDto dataLogDto = DataConverter.convert(dataLogPo, DataLogDto::new);
        //执行完数据补偿，同步更新日志
        dataLogDto = ResultUtils.getData(dataCompensateClient.retry(dataLogDto));
        if (null == dataLogDto) {
            this.delete(dataLogPo.getId());
        } else {
            this.updateById(DataConverter.convert(dataLogDto, DataLogPo::new));
        }
        return dataLogDto;
    }

    @Autowired
    public void setFeignClientFactory(FeignClientFactory feignClientFactory) {
        this.feignClientFactory = feignClientFactory;
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 当使用@Autowired 注解的时候，其实默认required=true，表示注入的时候，该bean必须存在，否则就会注入失败
     * required=false,表示当前要注入的bean，如果有直接注入，没有跳过，不会报错
     */
    @Autowired(required = false)
    public void setEmailAlerter(EmailAlerter emailAlerter) {
        this.emailAlerter = emailAlerter;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addDataLog(DataLogPo dataLogPo) {
        this.insert(dataLogPo);
        if (Boolean.FALSE.equals(dataLogPo.getStatus())) {
            this.reportService.saveCallDetail(CacheKeys.DATA_CALL_FAILURE);
            if (null != emailAlerter) {
                this.emailAlerter.alert(dataLogPo);
            }
        }
    }
}
