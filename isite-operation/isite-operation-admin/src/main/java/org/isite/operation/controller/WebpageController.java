package org.isite.operation.controller;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.enums.TerminalType;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.constants.UserAgent;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.mq.Message;
import org.isite.commons.web.mq.Publisher;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.activity.ActivityAssert;
import org.isite.operation.cache.ActivityCache;
import org.isite.operation.converter.ActivityConverter;
import org.isite.operation.converter.WebpageConverter;
import org.isite.operation.mq.WebpageProducer;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.po.WebpagePo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeService;
import org.isite.operation.service.TaskService;
import org.isite.operation.service.WebpageService;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.dto.WebpagePutDto;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Webpage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 活动页面 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@RestController
public class WebpageController extends BaseController {
    private ActivityCache activityCache;
    private WebpageService webpageService;
    private ActivityService activityService;
    private TaskService taskService;
    private PrizeService prizeService;

    /**
     * 分页查询活动页面
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/webpages")
    public PageResult<Webpage> findPage(PageRequest<Integer> request) {
        try (Page<WebpagePo> page = webpageService.findPage(
                PageQueryConverter.toPageQuery(request, () -> WebpageConverter.toWebpagePo(request.getQuery())))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), Webpage::new), page.getTotal());
        }
    }

    /**
     * 用户访问活动页面，不需要登录就可以访问（从缓存读取数据）。被邀请人url携带邀请码：invite，发送行为消息触发运营任务
     */
    @GetMapping(UrlConstants.URL_API + OperationUrls.URL_OPERATION + "/webpage/{activityId}")
    @Publisher(messages = @Message(queues = OperationConstants.QUEUE_OPERATION_EVENT, producer = WebpageProducer.class))
    public Result<String> getWebpage(@PathVariable("activityId") int activityId) {
        Activity activity = activityCache.getActivity(activityId);
        Assert.notNull(activity, MessageUtils.getMessage(ActivityController.KEY_ACTIVITY_NOT_FOUND, ActivityController.VALUE_ACTIVITY_NOT_FOUND));
        return toResult(activityCache.getWebpage(activity, UserAgent.getTerminalType()));
    }

    /**
     * 获取模板页面（用于管理后台查询数据）
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/webpage/{activityId}/{terminalType}")
    public Result<Webpage> getWebpage(@PathVariable("activityId") int activityId,
                           @PathVariable("terminalType") TerminalType terminalType) {
        return toResult(DataConverter.convert(webpageService.getWebpage(activityId, terminalType), Webpage::new));
    }

    /**
     * 活动页面预览（用于管理后台从数据库查询数据）
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/webpage/{activityId}/{terminalType}/preview")
    public Result<String> getPreview(@PathVariable("activityId") int activityId,
                           @PathVariable("terminalType") TerminalType terminalType) {
        Activity activity = ActivityConverter.toActivity(activityService.getActivity(activityId, null),
                taskService.findList(TaskPo::getActivityId, activityId),
                prizeService.findList(PrizePo::getActivityId, activityId));
        return toResult(webpageService.getWebpage(activity, terminalType));
    }

    /**
     * 更新活动页面，活动ID不能变更
     */
    @PutMapping(OperationUrls.URL_OPERATION + "/webpage/{activityId}")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> updateWebpage(@PathVariable("activityId") Integer activityId,
                                         @Validated @RequestBody WebpagePutDto webpagePutDto) {
        ActivityAssert.notOnline(activityService.get(activityId).getStatus());
        Assert.isTrue(webpageService.get(webpagePutDto.getId()).getActivityId().equals(activityId), new IllegalParameterError());
        return toResult(webpageService.updateSelectiveById(DataConverter.convert(webpagePutDto, WebpagePo::new)));
    }

    @Autowired
    public void setActivityCache(ActivityCache activityCache) {
        this.activityCache = activityCache;
    }

    @Autowired
    public void setWebpageService(WebpageService webpageService) {
        this.webpageService = webpageService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

}
