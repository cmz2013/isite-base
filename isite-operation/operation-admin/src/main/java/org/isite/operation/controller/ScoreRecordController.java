package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.support.vo.ScoreRecord;
import org.isite.operation.support.vo.ScoreView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.converter.ScoreRecordConverter.toScoreRecordSelectivePo;
import static org.isite.operation.support.constants.UrlConstants.PUT_USE_VIP_SCORE;
import static org.isite.operation.support.constants.UrlConstants.URL_OPERATION;
import static org.isite.operation.support.enums.ScoreType.ACTIVITY_SCORE;

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
        try (Page<ScoreRecordPo> page = scoreRecordService.findVipScoreRecords(getUserId(), toPageQuery(request, null))) {
            return toPageResult(request, convert(page.getResult(), ScoreRecord::new), page.getTotal());
        }
    }

    @PutMapping(PUT_USE_VIP_SCORE)
    public Result<Boolean> useVipScore(@RequestParam(("score")) Integer score) {
        return toResult(scoreRecordService.useVipScore(getUserId(), score));
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }
}
