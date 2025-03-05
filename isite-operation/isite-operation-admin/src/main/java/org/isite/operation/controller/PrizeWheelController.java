package org.isite.operation.controller;

import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.service.PrizeWheelService;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 抽奖活动，完成奖品任务获取抽奖次数
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class PrizeWheelController extends BaseController {
    private PrizeWheelService prizeWheelService;
    private OngoingActivityService ongoingActivityService;

    /**
     * 用户抽奖
     */
    @PutMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/wheel/{activityId}")
    public Result<Prize> drawPrize(@PathVariable("activityId") Integer activityId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        Assert.notNull(activity, MessageUtils.getMessage(ActivityController.KEY_ACTIVITY_NOT_FOUND, ActivityController.VALUE_ACTIVITY_NOT_FOUND));
        return toResult(prizeWheelService.drawPrize(activity, TransmittableHeaders.getUserId()));
    }

    @Autowired
    public void setOngoingActivityService(OngoingActivityService ongoingActivityService) {
        this.ongoingActivityService = ongoingActivityService;
    }

    @Autowired
    public void setPrizeWheelService(PrizeWheelService prizeWheelService) {
        this.prizeWheelService = prizeWheelService;
    }
}
