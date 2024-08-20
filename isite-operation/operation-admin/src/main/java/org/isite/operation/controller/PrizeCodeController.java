package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.data.dto.PrizeCodeDto;
import org.isite.operation.data.vo.PrizeCode;
import org.isite.operation.po.PrizeCodePo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeCodeService;
import org.isite.operation.service.PrizeService;
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

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.operation.converter.PrizeCodeConverter.toPrizeCodePo;
import static org.isite.operation.data.constants.CacheKey.LOCK_ACTIVITY;
import static org.isite.operation.data.constants.UrlConstants.URL_OPERATION;
import static org.isite.operation.activity.ActivityAssert.notOnline;

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
    @GetMapping(URL_OPERATION + "/prize/{prizeId}/codes")
    public PageResult<PrizeCode> findPage(
            @PathVariable("prizeId") Integer prizeId, PageRequest<PrizeCodeDto> request) {
        try (Page<PrizeCodePo> page = prizeCodeService.findPage(
                toPageQuery(request, () -> toPrizeCodePo(prizeId, request.getQuery())))) {
            return toPageResult(request, convert(page.getResult(), PrizeCode::new), page.getTotal());
        }
    }

    /**
     * 添加兑奖码
     */
    @PostMapping(URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/codes")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> addPrizeCodes(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId,
            @Validated @RequestBody @NotEmpty Set<String> codes) {
        notOnline(activityService.get(activityId).getStatus());
        PrizePo prizePo = prizeService.get(prizeId);
        isTrue(prizePo.getActivityId().equals(activityId), new IllegalParameterError());
        return toResult(prizeCodeService.addPrizeCodes(prizePo, codes));
    }

    /**
     * 删除兑奖码
     */
    @DeleteMapping(URL_OPERATION + "/activity/{activityId}/prize/{prizeId}/codes")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> deletePrizeCodes(
            @PathVariable("activityId") Integer activityId, @PathVariable("prizeId") Integer prizeId,
            @Validated @RequestParam("ids") @NotEmpty List<Integer> ids) {
        notOnline(activityService.get(activityId).getStatus());
        PrizePo prizePo = prizeService.get(prizeId);
        isTrue(prizePo.getActivityId().equals(activityId), new IllegalParameterError());
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
