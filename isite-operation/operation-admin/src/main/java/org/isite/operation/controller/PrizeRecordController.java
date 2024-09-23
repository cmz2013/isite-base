package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.service.PrizeRecordService;
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

import static org.apache.commons.lang.BooleanUtils.isNotTrue;
import static org.isite.commons.cloud.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.cloud.utils.VoUtils.get;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getEmployeeId;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.misc.data.enums.ObjectType.TENANT_EMPLOYEE;
import static org.isite.operation.activity.ActivityAssert.notOnline;
import static org.isite.operation.controller.ActivityController.KEY_ACTIVITY_NOT_FOUND;
import static org.isite.operation.controller.ActivityController.VALUE_ACTIVITY_NOT_FOUND;
import static org.isite.operation.support.constants.CacheKey.LOCK_ACTIVITY;
import static org.isite.operation.support.constants.UrlConstants.URL_OPERATION;

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
    @PostMapping(URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/user/{userId}")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> addPrizeRecord(@PathVariable("activityId") Integer activityId,
                                          @PathVariable("prizeId") Integer prizeId,
                                          @PathVariable("userId") long userId) {
        ActivityPo activityPo = activityService.get(activityId);
        notOnline(activityPo.getStatus());
        return toResult(prizeRecordService.addPrizeRecord(activityPo, prizeId, userId, getEmployeeId()));
    }

    /**
     * 管理员删除给用户赠送的领奖记录
     */
    @DeleteMapping(URL_OPERATION + "/activity/{activityId}/prize/record/{recordId}")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> deletePrizeRecord(
            @PathVariable("activityId") Integer activityId, @PathVariable("recordId") Long recordId) {
        notOnline(activityService.get(activityId).getStatus());
        PrizeRecordPo prizeRecordPo = prizeRecordService.get(recordId);
        isTrue(prizeRecordPo.getActivityId().equals(activityId) &&
                prizeRecordPo.getObjectType().equals(TENANT_EMPLOYEE), new IllegalParameterError());
        isTrue(isNotTrue(prizeRecordPo.getReceiveStatus()), getMessage("received.notDelete",
                "prize record has been received, cannot delete"));
        return toResult(prizeRecordService.deletePrizeRecord(prizeRecordPo));
    }

    /**
     * 用户领取奖品。从缓存查询活动和奖品信息
     * 如果是实物类型奖品，领取成功后弹出窗口，用户提交收件人信息
     */
    @PutMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/receive")
    public Result<Integer> receivePrize(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId) {
        Activity activity = ongoingActivityService.getOngoingActivity(activityId);
        notNull(activity, getMessage(KEY_ACTIVITY_NOT_FOUND, VALUE_ACTIVITY_NOT_FOUND));
        Prize prize = get(activity.getPrizes(), prizeId);
        notNull(prize, getMessage("prize.notFound", "prize not found"));
        return toResult(prizeRecordService.receivePrize(activity, prize, getUserId()));
    }

    /**
     * 查询领奖记录（用于管理后台查询数据）
     */
    @GetMapping(URL_OPERATION + "/prize/records")
    public PageResult<PrizeRecord> findPage(PageRequest<PrizeRecordDto> request) {
        try (Page<PrizeRecordPo> page = prizeRecordService.findPage(toPageQuery(request, PrizeRecordPo::new))) {
            return toPageResult(request, convert(page.getResult(), PrizeRecord::new), page.getTotal());
        }
    }

    /**
     * 用户查询已领取的奖品
     */
    @GetMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/prizes")
    public Result<List<PrizeRecord>> findUserPrizes(@PathVariable("activityId") Integer activityId) {
        return toResult(convert(prizeRecordService.findReceived(activityId, getUserId()), PrizeRecord::new));
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
