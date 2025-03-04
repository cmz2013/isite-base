package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.operation.converter.ScoreRecordConverter;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.enums.ScoreType;
import org.isite.operation.support.vo.ScoreRecord;
import org.isite.operation.support.vo.VipScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ScoreRecordController extends BaseController {
    private ScoreRecordService scoreRecordService;

    /**
     * 用户查询活动积分总值
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/activity/{activityId}/score")
    public Result<VipScore> findActivityScore(@PathVariable("activityId") Integer activityId) {
        return toResult(new VipScore(scoreRecordService.sumActivityScore(activityId, TransmittableHeaders.getUserId())));
    }

    /**
     * 用户查询活动积分记录
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/activity/{activityId}/score/records")
    public PageResult<ScoreRecord> findPage(@PathVariable("activityId") Integer activityId, PageRequest<?> request) {
        try (Page<ScoreRecordPo> page = scoreRecordService.findPage(PageQueryConverter.toPageQuery(request,
                () -> ScoreRecordConverter.toScoreRecordSelectivePo(activityId, TransmittableHeaders.getUserId(), ScoreType.ACTIVITY_SCORE)))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), ScoreRecord::new), page.getTotal());
        }
    }

    /**
     * 用户查询VIP总积分，VIP积分有效期为1年
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/score")
    public Result<VipScore> findVipScore() {
        return toResult(scoreRecordService.findVipScore(TransmittableHeaders.getUserId()));
    }

    /**
     * 用户查询VIP积分记录，VIP积分有效期为1年
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/score/records")
    public PageResult<ScoreRecord> findPage(PageRequest<?> request) {
        try (Page<ScoreRecordPo> page = scoreRecordService.findScoreRecords(PageQueryConverter.toPageQuery(request, () -> {
                    ScoreRecordPo scoreRecordPo = new ScoreRecordPo();
                    scoreRecordPo.setScoreType(ScoreType.VIP_SCORE);
                    scoreRecordPo.setUserId(TransmittableHeaders.getUserId());
                    return scoreRecordPo;
                }), LocalDateTime.now().minusYears(Constants.ONE))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), ScoreRecord::new), page.getTotal());
        }
    }

    @Validated
    @PutMapping(OperationUrls.PUT_USE_VIP_SCORE)
    public Result<?> useVipScore(@RequestParam(("score")) @NotNull Integer score) {
        Assert.isTrue(score > Constants.ZERO, "score must be greater than 0");
        return toResult(() -> scoreRecordService.useVipScore(TransmittableHeaders.getUserId(), score));
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }
}
