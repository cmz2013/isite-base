package org.isite.operation.controller;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.enums.TerminalType;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.mq.Message;
import org.isite.commons.web.mq.Publisher;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.cache.ActivityCache;
import org.isite.operation.converter.ActivityConverter;
import org.isite.operation.data.dto.WebpagePutDto;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Webpage;
import org.isite.operation.mq.WebpageProducer;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.po.WebpagePo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeService;
import org.isite.operation.service.TaskService;
import org.isite.operation.service.WebpageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.constants.UrlConstants.URL_API;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.web.constants.UserAgent.getTerminalType;
import static org.isite.operation.activity.ActivityAssert.notOnline;
import static org.isite.operation.controller.ActivityController.KEY_ACTIVITY_NOT_FOUND;
import static org.isite.operation.controller.ActivityController.VALUE_ACTIVITY_NOT_FOUND;
import static org.isite.operation.converter.WebpageConverter.toWebpagePo;
import static org.isite.operation.data.constants.CacheKey.LOCK_ACTIVITY;
import static org.isite.operation.data.constants.OperationConstants.QUEUE_OPERATION_EVENT;
import static org.isite.operation.data.constants.UrlConstants.URL_OPERATION;

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
    @GetMapping(URL_OPERATION + "/webpages")
    public PageResult<Webpage> findPage(PageRequest<Integer> request) {
        try (Page<WebpagePo> page = webpageService.findPage(toPageQuery(request, () -> toWebpagePo(request.getQuery())))) {
            return toPageResult(request, convert(page.getResult(), Webpage::new), page.getTotal());
        }
    }

    /**
     * 用户访问活动页面，不需要登录就可以访问（从缓存读取数据）。被邀请人url携带邀请码：invite，发送行为消息触发运营任务
     */
    @GetMapping(URL_API + URL_OPERATION + "/webpage/{activityId}")
    @Publisher(messages = @Message(queues = QUEUE_OPERATION_EVENT, producer = WebpageProducer.class))
    public Result<String> getWebpage(@PathVariable("activityId") int activityId) {
        Activity activity = activityCache.getActivity(activityId);
        notNull(activity, getMessage(KEY_ACTIVITY_NOT_FOUND, VALUE_ACTIVITY_NOT_FOUND));
        return toResult(activityCache.getWebpage(activity, getTerminalType()));
    }

    /**
     * 获取模板页面（用于管理后台查询数据）
     */
    @GetMapping(URL_OPERATION + "/webpage/{activityId}/{terminalType}")
    public Result<Webpage> getWebpage(@PathVariable("activityId") int activityId,
                           @PathVariable("terminalType") TerminalType terminalType) {
        return toResult(convert(webpageService.getWebpage(activityId, terminalType), Webpage::new));
    }

    /**
     * 活动页面预览（用于管理后台从数据库查询数据）
     */
    @GetMapping(URL_OPERATION + "/webpage/{activityId}/{terminalType}/preview")
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
    @PutMapping(URL_OPERATION + "/webpage/{activityId}")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> updateWebpage(
            @PathVariable("activityId") Integer activityId,
            @Validated(Update.class) @RequestBody WebpagePutDto webpagePutDto) {
        notOnline(activityService.get(activityId).getStatus());
        isTrue(webpageService.get(webpagePutDto.getId()).getActivityId().equals(activityId), new IllegalParameterError());
        return toResult(webpageService.updateSelectiveById(convert(webpagePutDto, WebpagePo::new)));
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
