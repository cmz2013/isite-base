package org.isite.operation.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.data.dto.PrizePostDto;
import org.isite.operation.data.dto.PrizePutDto;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeCodeService;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.operation.data.constants.CacheKey.LOCK_ACTIVITY;
import static org.isite.operation.data.constants.OperationConstants.FIELD_TOTAL_INVENTORY;
import static org.isite.operation.data.constants.UrlConstants.URL_OPERATION;
import static org.isite.operation.data.enums.PrizeType.PRIZE_CODE;
import static org.isite.operation.activity.ActivityAssert.notExistTaskRecord;
import static org.isite.operation.activity.ActivityAssert.notOnline;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class PrizeController extends BaseController {

    private PrizeService prizeService;
    private PrizeRecordService prizeRecordService;
    private ActivityService activityService;
    private PrizeCodeService prizeCodeService;

    /**
     * 用于管理后台查询活动奖品，一个活动的奖品个数不能超过1000
     */
    @GetMapping(URL_OPERATION + "/activity/{activityId}/prizes")
    public Result<List<Prize>> findPrizes(@PathVariable("activityId") Integer activityId) {
        return toResult(convert(prizeService.findList(PrizePo::getActivityId, activityId), Prize::new));
    }

    /**
     * 添加活动奖品，一个活动的奖品个数不能超过1000
     */
    @PostMapping(URL_OPERATION + "/prize")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#prizePostDto.activityId"))
    public Result<Integer> addPrize(@Validated @RequestBody PrizePostDto prizePostDto) {
        notOnline(activityService.get(prizePostDto.getActivityId()).getStatus());
        isTrue(THOUSAND > prizeService.count(PrizePo::getActivityId, prizePostDto.getActivityId()),
                getMessage("prize.total.error", "the total of prizes cannot exceed 1000"));
        return toResult(prizeService.insert(convert(prizePostDto, PrizePo::new)));
    }

    /**
     * 更新运营奖品，活动ID和奖品类型不能变更
     * 兑奖码类型的奖品，总库存在导出和删除兑奖码时自动维护
     */
    @PutMapping("/activity/{activityId}/prize")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> editPrize(
            @PathVariable("activityId") Integer activityId,
            @Validated(Update.class) @RequestBody PrizePutDto prizePutDto) {
        notOnline(activityService.get(activityId).getStatus());
        PrizePo prizePo = prizeService.get(prizePutDto.getId());
        isTrue(prizePo.getActivityId().equals(activityId), new IllegalParameterError());
        isTrue(prizePutDto.getTotalInventory() == ZERO ||
                prizePutDto.getTotalInventory() >= prizePo.getConsumeInventory(), getMessage("prize.inventory.error",
                "totalInventory cannot be less than consumeInventory"));
        return toResult(prizeService.updateSelectiveById(PRIZE_CODE.equals(prizePo.getPrizeType()) ?
                convert(prizePutDto, PrizePo::new, FIELD_TOTAL_INVENTORY) :
                convert(prizePutDto, PrizePo::new)));
    }

    /**
     * 删除活动奖品
     */
    @DeleteMapping("/activity/{activityId}/prize/{prizeId}")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> deletePrize(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId) {
        notOnline(activityService.get(activityId).getStatus());
        PrizePo prizePo = prizeService.get(prizeId);
        isTrue(prizePo.getActivityId().equals(activityId), new IllegalParameterError());
        notExistTaskRecord(prizeRecordService.exists(PrizeRecordPo::getPrizeId, prizeId));
        return toResult(PRIZE_CODE.equals(prizePo.getPrizeType()) ?
                prizeCodeService.deletePrize(prizeId) : prizeService.delete(prizeId));
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setPrizeCodeService(PrizeCodeService prizeCodeService) {
        this.prizeCodeService = prizeCodeService;
    }
}
