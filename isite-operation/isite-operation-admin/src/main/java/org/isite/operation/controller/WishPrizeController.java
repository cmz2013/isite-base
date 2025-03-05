package org.isite.operation.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.service.WishPrizeService;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 许愿活动（积分活动示例），活动步骤：
 * 1) 用户选择礼品完成许愿，在prize_record中创建一条待抽奖的任务记录
 * 2）用户通过完成积分任务获取足够的积分
 * 3）用户使用积分兑换心愿礼品，心愿礼品库存不足时可以变更
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class WishPrizeController extends BaseController {
    private WishPrizeService wishPrizeService;
    private OngoingActivityService ongoingActivityService;
    private ScoreRecordService scoreRecordService;

    /**
     * 保存许愿记录，从缓存查询活动和奖品信息
     */
    @PostMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/wish/{activityId}/prize/{prizeId}")
    public Result<Integer> addWishPrize(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        Assert.notNull(activity, MessageUtils.getMessage(ActivityController.KEY_ACTIVITY_NOT_FOUND, ActivityController.VALUE_ACTIVITY_NOT_FOUND));
        Prize prize = VoUtils.get(activity.getPrizes(), prizeId);
        Assert.notNull(prize, MessageUtils.getMessage("prize.notFound", "prize not found"));
        return toResult(wishPrizeService.addWishPrize(activity, prize, TransmittableHeaders.getUserId()));
    }

    /**
     * 查询用户可用积分
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/wish/{activityId}/score")
    public Result<Integer> getAvailableScore(@PathVariable("activityId") Integer activityId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        Assert.notNull(activity, MessageUtils.getMessage(ActivityController.KEY_ACTIVITY_NOT_FOUND, ActivityController.VALUE_ACTIVITY_NOT_FOUND));
        return toResult(scoreRecordService.sumActivityScore(activity.getId(), TransmittableHeaders.getUserId()));
    }

    /**
     * 变更心愿礼品，从缓存查询活动和奖品信息
     */
    @PutMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/wish/{activityId}/prize/{prizeId}")
    public Result<Long> updateWishPrize(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId) {
        Prize prize = ongoingActivityService.getOngoingPrize(activityId, prizeId);
        Assert.notNull(prize, MessageUtils.getMessage("prize.notFound", "prize not found"));
        return toResult(wishPrizeService.updateWishPrize(activityId, TransmittableHeaders.getUserId(), prize));
    }

    /**
     * 实现心愿（兑换心愿礼品），从缓存查询活动和奖品信息
     */
    @PutMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/wish/{activityId}/prize/{prizeId}/receive")
    public Result<PrizeRecord> receiveWishPrize(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        Assert.notNull(activity, MessageUtils.getMessage(ActivityController.KEY_ACTIVITY_NOT_FOUND, ActivityController.VALUE_ACTIVITY_NOT_FOUND));
        return toResult(DataConverter.convert(wishPrizeService.receiveWishPrize(
                activity, prizeId, TransmittableHeaders.getUserId()), PrizeRecord::new));
    }

    @Autowired
    public void setWishPrizeService(WishPrizeService wishPrizeService) {
        this.wishPrizeService = wishPrizeService;
    }

    @Autowired
    public void setOngoingActivityService(OngoingActivityService ongoingActivityService) {
        this.ongoingActivityService = ongoingActivityService;
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }
}
