package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.support.vo.ScoreRecord;
import org.isite.operation.support.vo.ScoreView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.converter.ScoreRecordConverter.toScoreRecordSelectivePo;
import static org.isite.operation.support.constants.OperationUrls.PUT_USE_VIP_SCORE;
import static org.isite.operation.support.constants.OperationUrls.URL_OPERATION;
import static org.isite.operation.support.enums.ScoreType.ACTIVITY_SCORE;
import static org.isite.operation.support.enums.ScoreType.VIP_SCORE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ScoreRecordController extends BaseController {

    private ScoreRecordService scoreRecordService;

    /**
     * 用户查询活动积分总值
     */
    @GetMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/score")
    public Result<ScoreView> findActivityScore(@PathVariable("activityId") Integer activityId) {
        return toResult(new ScoreView(scoreRecordService.sumActivityScore(activityId, getUserId())));
    }

    /**
     * 用户查询活动积分记录
     */
    @GetMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/score/records")
    public PageResult<ScoreRecord> findPage(@PathVariable("activityId") Integer activityId, PageRequest<?> request) {
        try (Page<ScoreRecordPo> page = scoreRecordService.findPage(
                toPageQuery(request, () -> toScoreRecordSelectivePo(activityId, getUserId(), ACTIVITY_SCORE)))) {
            return toPageResult(request, convert(page.getResult(), ScoreRecord::new), page.getTotal());
        }
    }

    /**
     * 用户查询VIP总积分，VIP积分有效期为1年
     */
    @GetMapping(URL_MY + URL_OPERATION + "/score")
    public Result<ScoreView> findVipScore() {
        return toResult(new ScoreView(scoreRecordService.sumVipScore(getUserId()),
                scoreRecordService.aboutToExpireVipScore(getUserId())));
    }

    /**
     * 用户查询VIP积分记录，VIP积分有效期为1年
     */
    @GetMapping(URL_MY + URL_OPERATION + "/score/records")
    public PageResult<ScoreRecord> findPage(PageRequest<?> request) {
        try (Page<ScoreRecordPo> page = scoreRecordService.findScoreRecords(toPageQuery(request, () -> {
                    ScoreRecordPo scoreRecordPo = new ScoreRecordPo();
                    scoreRecordPo.setScoreType(VIP_SCORE);
                    scoreRecordPo.setUserId(getUserId());
                    return scoreRecordPo;
                }), LocalDateTime.now().minusYears(ONE))) {
            return toPageResult(request, convert(page.getResult(), ScoreRecord::new), page.getTotal());
        }
    }

    @Validated
    @PutMapping(PUT_USE_VIP_SCORE)
    public Result<?> useVipScore(@RequestParam(("score")) @NotNull Integer score) {
        isTrue(score > ZERO, "score must be greater than 0");
        return toResult(() -> scoreRecordService.useVipScore(getUserId(), score));
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }
}
