package org.isite.operation.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.jpa.data.PageQuery;
import org.isite.operation.mapper.ScoreRecordMapper;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.enums.ScoreType;
import org.isite.operation.support.vo.VipScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ScoreRecordService extends TaskRecordService<ScoreRecordPo> {

    @Autowired
    public ScoreRecordService(ScoreRecordMapper scoreRecordMapper) {
        super(scoreRecordMapper);
    }

    /**
     * 统计积分记录
     */
    public int countScoreRecord(int activityId, int taskId, @Nullable LocalDateTime startTime, long userId) {
        Weekend<ScoreRecordPo> weekend = Weekend.of(ScoreRecordPo.class);
        WeekendCriteria<ScoreRecordPo, Object> criteria = weekend.weekendCriteria()
                .andEqualTo(ScoreRecordPo::getUserId, userId)
                .andEqualTo(ScoreRecordPo::getActivityId, activityId)
                .andEqualTo(ScoreRecordPo::getTaskId, taskId);
        if (null != startTime) {
            criteria.andGreaterThanOrEqualTo(ScoreRecordPo::getFinishTime, startTime);
        }
        return this.count(weekend);
    }

    /**
     * 统计用户可用的VIP积分，VIP积分有效期为一年
     */
    public int sumVipScore(long userId) {
        return ((ScoreRecordMapper) getMapper()).sumAvailableScore(null, userId,
                ScoreType.VIP_SCORE, LocalDateTime.now().minusYears(Constants.ONE), null);
    }

    /**
     * 统计用户快要过期的VIP积分，VIP积分有效期为一年
     */
    public int aboutToExpireVipScore(long userId) {
        return ((ScoreRecordMapper) getMapper()).sumAvailableScore(null, userId,
                ScoreType.VIP_SCORE, LocalDateTime.now().minusYears(Constants.ONE), LocalDateTime.now().minusMonths(11));
    }

    /**
     * 统计用户可用的活动积分
     */
    public int sumActivityScore(int activityId, long userId) {
        return ((ScoreRecordMapper) getMapper()).sumAvailableScore(
                activityId, userId, ScoreType.ACTIVITY_SCORE, null, null);
    }

    /**
     * @Description 使用活动积分。
     * 在同一个事务中更新了数据，但在提交之前查询，查询结果将反映更新后的数据。
     * 这是因为事务中的所有操作都是在同一个数据库会话中进行的
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY_USER, keys = {"#activityId", "#userId"}))
    public void useActivityScore(int activityId, long userId, int score) {
        ScoreRecordMapper mapper = ((ScoreRecordMapper) getMapper());
        ScoreRecordPo scoreRecordPo = mapper.selectOneAvailableScore(activityId, userId, ScoreType.ACTIVITY_SCORE, null);
        while(score > Constants.ZERO && null != scoreRecordPo) {
            int available = scoreRecordPo.getScoreValue() - scoreRecordPo.getUsedScore();
            if (available >= score) {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getUsedScore() + score);
                score = Constants.ZERO;
            } else {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getScoreValue());
                score -= available;
                scoreRecordPo = mapper.selectOneAvailableScore(activityId, userId, ScoreType.ACTIVITY_SCORE, null);
            }
        }
        Assert.isTrue(Constants.ZERO == score, "not enough activity score");
    }

    /**
     * @Description 使用VIP积分。
     * 在同一个事务中更新了数据，但在提交之前查询，查询结果将反映更新后的数据。
     * 这是因为事务中的所有操作都是在同一个数据库会话中进行的
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_USER_VIP_SCORE, keys = "#userId"))
    public void useVipScore(long userId, int score) {
        ScoreRecordMapper mapper = ((ScoreRecordMapper) getMapper());
        LocalDateTime startTime = LocalDateTime.now().minusYears(Constants.ONE);
        ScoreRecordPo scoreRecordPo = mapper.selectOneAvailableScore(null, userId, ScoreType.VIP_SCORE, startTime);
        while(score > Constants.ZERO && null != scoreRecordPo) {
            int available = scoreRecordPo.getScoreValue() - scoreRecordPo.getUsedScore();
            if (available >= score) {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getUsedScore() + score);
                score = Constants.ZERO;
            } else {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getScoreValue());
                score -= available;
                scoreRecordPo = mapper.selectOneAvailableScore(null, userId, ScoreType.VIP_SCORE, startTime);
            }
        }
        Assert.isTrue(Constants.ZERO == score, "not enough vip score");
    }

    public Page<ScoreRecordPo> findScoreRecords(PageQuery<ScoreRecordPo> pageQuery, @Nullable LocalDateTime startTime) {
        //当前线程紧跟着的第一个select方法会被分页
        try (Page<ScoreRecordPo> page = PageMethod.offsetPage(pageQuery.getOffset(), pageQuery.getPageSize())) {
            String orderBy = pageQuery.orderBy();
            if (StringUtils.isNotBlank(orderBy)) {
                page.setOrderBy(orderBy);
            }
            ScoreRecordMapper mapper = ((ScoreRecordMapper) getMapper());
            return (Page<ScoreRecordPo>) mapper.selectScoreRecord(pageQuery.getPo(), startTime);
        }
    }

    public VipScore findVipScore(Long userId) {
        return new VipScore(sumVipScore(userId), aboutToExpireVipScore(userId));
    }
}
