package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.activity.ActivityAssert;
import org.isite.operation.converter.PrizeCodeConverter;
import org.isite.operation.po.PrizeCodePo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeCodeService;
import org.isite.operation.service.PrizeService;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.dto.PrizeCodeDto;
import org.isite.operation.support.vo.PrizeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class PrizeCodeController extends BaseController {
    private ActivityService activityService;
    private PrizeService prizeService;
    private PrizeCodeService prizeCodeService;

    /**
     * 分页查询兑奖码
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/prize/{prizeId}/codes")
    public PageResult<PrizeCode> findPage(
            @PathVariable("prizeId") Integer prizeId, PageRequest<PrizeCodeDto> request) {
        try (Page<PrizeCodePo> page = prizeCodeService.findPage(PageQueryConverter.toPageQuery(
                request, () -> PrizeCodeConverter.toPrizeCodeSelectivePo(prizeId, request.getQuery())))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), PrizeCode::new), page.getTotal());
        }
    }

    /**
     * 添加兑奖码
     */
    @PostMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/codes")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> addPrizeCodes(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId,
            @Validated @RequestBody @NotEmpty Set<String> codes) {
        ActivityAssert.notOnline(activityService.get(activityId).getStatus());
        PrizePo prizePo = prizeService.get(prizeId);
        Assert.isTrue(prizePo.getActivityId().equals(activityId), new IllegalParameterError());
        return toResult(prizeCodeService.addPrizeCodes(prizePo, codes));
    }

    /**
     * 删除兑奖码
     */
    @DeleteMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/codes")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> deletePrizeCodes(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId,
            @Validated @RequestParam("ids") @NotEmpty List<Integer> ids) {
        ActivityAssert.notOnline(activityService.get(activityId).getStatus());
        PrizePo prizePo = prizeService.get(prizeId);
        Assert.isTrue(prizePo.getActivityId().equals(activityId), new IllegalParameterError());
        return toResult(prizeCodeService.deletePrizeCodes(prizePo, ids));
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @Autowired
    public void setPrizeCodeService(PrizeCodeService prizeCodeService) {
        this.prizeCodeService = prizeCodeService;
    }
}
