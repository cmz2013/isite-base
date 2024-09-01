package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.operation.support.dto.ScoreRecordDto;
import org.isite.operation.support.vo.ScoreRecord;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.support.constants.UrlConstants.URL_OPERATION;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ScoreRecordController extends BaseController {

    private ScoreRecordService scoreRecordService;

    /**
     * 查询积分记录（用于管理后台查询数据）
     */
    @GetMapping(URL_OPERATION + "/score/records")
    public PageResult<ScoreRecord> findPage(PageRequest<ScoreRecordDto> request) {
        try (Page<ScoreRecordPo> page = scoreRecordService.findPage(toPageQuery(request, ScoreRecordPo::new))) {
            return toPageResult(request, convert(page.getResult(), ScoreRecord::new), page.getTotal());
        }
    }

    /**
     * 查询当前用户积分记录（用于用户查询数据）
     */
    @GetMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/score/records")
    public Result<List<ScoreRecord>> findScoreRecords(@PathVariable("activityId") Integer activityId) {
        return toResult(convert(scoreRecordService.findList(activityId, getUserId()), ScoreRecord::new));
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }
}
