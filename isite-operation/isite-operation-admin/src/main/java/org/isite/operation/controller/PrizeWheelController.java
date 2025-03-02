package org.isite.operation.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.service.PrizeWheelService;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.controller.ActivityController.KEY_ACTIVITY_NOT_FOUND;
import static org.isite.operation.controller.ActivityController.VALUE_ACTIVITY_NOT_FOUND;
import static org.isite.operation.support.constants.OperationUrls.URL_OPERATION;

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
    @PutMapping(URL_MY + URL_OPERATION + "/wheel/{activityId}")
    public Result<Prize> drawPrize(@PathVariable("activityId") Integer activityId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        notNull(activity, getMessage(KEY_ACTIVITY_NOT_FOUND, VALUE_ACTIVITY_NOT_FOUND));
        return toResult(prizeWheelService.drawPrize(activity, getUserId()));
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
