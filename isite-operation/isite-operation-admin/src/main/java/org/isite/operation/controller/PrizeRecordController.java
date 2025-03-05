package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.apache.commons.lang3.BooleanUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.activity.ActivityAssert;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.dto.PrizeRecordDto;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class PrizeRecordController extends BaseController {
    private PrizeRecordService prizeRecordService;
    private ActivityService activityService;
    private OngoingActivityService ongoingActivityService;

    /**
     * 管理员给用户赠送领奖记录，设置必中奖品。prizeId=0时不设置必中
     */
    @PostMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/user/{userId}")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> addPrizeRecord(@PathVariable("activityId") Integer activityId,
                                          @PathVariable("prizeId") Integer prizeId,
                                          @PathVariable("userId") long userId) {
        ActivityPo activityPo = activityService.get(activityId);
        ActivityAssert.notOnline(activityPo.getStatus());
        return toResult(prizeRecordService.addPrizeRecord(activityPo, prizeId, userId, TransmittableHeaders.getEmployeeId()));
    }

    /**
     * 管理员删除给用户赠送的领奖记录
     */
    @DeleteMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}/prize/record/{recordId}")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> deletePrizeRecord(
            @PathVariable("activityId") Integer activityId, @PathVariable("recordId") Long recordId) {
        ActivityAssert.notOnline(activityService.get(activityId).getStatus());
        PrizeRecordPo prizeRecordPo = prizeRecordService.get(recordId);
        Assert.isTrue(prizeRecordPo.getActivityId().equals(activityId) &&
                prizeRecordPo.getObjectType().equals(ObjectType.TENANT_EMPLOYEE), new IllegalParameterError());
        Assert.isTrue(BooleanUtils.isNotTrue(prizeRecordPo.getReceiveStatus()), MessageUtils.getMessage("received.notDelete",
                "prize record has been received, cannot delete"));
        return toResult(prizeRecordService.deletePrizeRecord(prizeRecordPo));
    }

    /**
     * 用户领取奖品。从缓存查询活动和奖品信息
     * 如果是实物类型奖品，领取成功后弹出窗口，用户提交收件人信息
     */
    @PutMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/receive")
    public Result<Integer> receivePrize(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        Assert.notNull(activity, MessageUtils.getMessage(ActivityController.KEY_ACTIVITY_NOT_FOUND, ActivityController.VALUE_ACTIVITY_NOT_FOUND));
        Prize prize = VoUtils.get(activity.getPrizes(), prizeId);
        Assert.notNull(prize, MessageUtils.getMessage("prize.notFound", "prize not found"));
        return toResult(prizeRecordService.receivePrize(activity, prize, TransmittableHeaders.getUserId()));
    }

    /**
     * 查询领奖记录（用于管理后台查询数据）
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/prize/records")
    public PageResult<PrizeRecord> findPage(PageRequest<PrizeRecordDto> request) {
        try (Page<PrizeRecordPo> page = prizeRecordService.findPage(
                PageQueryConverter.toPageQuery(request, PrizeRecordPo::new))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), PrizeRecord::new), page.getTotal());
        }
    }

    /**
     * 用户查询已领取的奖品
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/activity/{activityId}/prizes")
    public Result<List<PrizeRecord>> findUserPrizes(@PathVariable("activityId") Integer activityId) {
        return toResult(DataConverter.convert(prizeRecordService.findReceived(
                activityId, TransmittableHeaders.getUserId()), PrizeRecord::new));
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setOngoingActivityService(OngoingActivityService ongoingActivityService) {
        this.ongoingActivityService = ongoingActivityService;
    }
}
