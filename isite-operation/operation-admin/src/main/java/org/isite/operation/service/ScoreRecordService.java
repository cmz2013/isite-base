package org.isite.operation.service;

import com.github.pagehelper.Page;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.jpa.data.PageQuery;
import org.isite.operation.mapper.ScoreRecordMapper;
import org.isite.operation.po.ScoreRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Date;

import static com.github.pagehelper.page.PageMethod.offsetPage;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.DateUtils.getTimeBeforeMonth;
import static org.isite.commons.lang.utils.DateUtils.getTimeBeforeYear;
import static org.isite.operation.support.constants.CacheKey.LOCK_ACTIVITY_SCORE_USER;
import static org.isite.operation.support.constants.CacheKey.LOCK_VIP_SCORE_USER;
import static org.isite.operation.support.enums.ScoreType.ACTIVITY_SCORE;
import static org.isite.operation.support.enums.ScoreType.VIP_SCORE;
import static tk.mybatis.mapper.weekend.Weekend.of;

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
    public int countScoreRecord(int activityId, int taskId, @Nullable Date startTime, long userId) {
        Weekend<ScoreRecordPo> weekend = of(ScoreRecordPo.class);
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
        return ((ScoreRecordMapper) getMapper()).sumAvailableScore(
                null, userId, VIP_SCORE, getTimeBeforeYear(ONE), null);
    }

    /**
     * 统计用户快要过期的VIP积分，VIP积分有效期为一年
     */
    public int aboutToExpireVipScore(long userId) {
        return ((ScoreRecordMapper) getMapper()).sumAvailableScore(
                null, userId, VIP_SCORE, getTimeBeforeYear(ONE), getTimeBeforeMonth(11));
    }

    /**
     * 统计用户可用的活动积分
     */
    public int sumActivityScore(int activityId, long userId) {
        return ((ScoreRecordMapper) getMapper()).sumAvailableScore(
                activityId, userId, ACTIVITY_SCORE, null, null);
    }

    /**
     * @Description 使用活动积分。
     * 在同一个事务中更新了数据，但在提交之前查询，查询结果将反映更新后的数据。
     * 这是因为事务中的所有操作都是在同一个数据库会话中进行的
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY_SCORE_USER, keys = {"#activityId", "#userId"}))
    public void useActivityScore(int activityId, long userId, int score) {
        ScoreRecordMapper mapper = ((ScoreRecordMapper) getMapper());
        ScoreRecordPo scoreRecordPo = mapper.selectOneAvailableScore(activityId, userId, ACTIVITY_SCORE, null);
        while(score > ZERO && null != scoreRecordPo) {
            int available = scoreRecordPo.getScoreValue() - scoreRecordPo.getUsedScore();
            if (available >= score) {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getUsedScore() + score);
                score = ZERO;
            } else {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getScoreValue());
                score -= available;
                scoreRecordPo = mapper.selectOneAvailableScore(activityId, userId, ACTIVITY_SCORE, null);
            }
        }
        isTrue(ZERO == score, "Not enough activity score");
    }

    /**
     * @Description 使用VIP积分。
     * 在同一个事务中更新了数据，但在提交之前查询，查询结果将反映更新后的数据。
     * 这是因为事务中的所有操作都是在同一个数据库会话中进行的
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = @Lock(name = LOCK_VIP_SCORE_USER, keys = "#userId"))
    public void useVipScore(long userId, int score) {
        ScoreRecordMapper mapper = ((ScoreRecordMapper) getMapper());
        Date startTime = getTimeBeforeYear(ONE);
        ScoreRecordPo scoreRecordPo = mapper.selectOneAvailableScore(null, userId, VIP_SCORE, startTime);
        while(score > ZERO && null != scoreRecordPo) {
            int available = scoreRecordPo.getScoreValue() - scoreRecordPo.getUsedScore();
            if (available >= score) {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getUsedScore() + score);
                score = ZERO;
            } else {
                this.updateById(scoreRecordPo.getId(), ScoreRecordPo::getUsedScore, scoreRecordPo.getScoreValue());
                score -= available;
                scoreRecordPo = mapper.selectOneAvailableScore(null, userId, VIP_SCORE, startTime);
            }
        }
        isTrue(ZERO == score, "Not enough vip score");
    }

    public Page<ScoreRecordPo> findVipScoreRecords(long userId, PageQuery<?> pageQuery) {
        //当前线程紧跟着的第一个select方法会被分页
        Page<ScoreRecordPo> page = offsetPage(pageQuery.getOffset(), pageQuery.getPageSize());
        String orderBy = pageQuery.orderBy();
        if (isNotBlank(orderBy)) {
            page.setOrderBy(orderBy);
        }
        ScoreRecordMapper mapper = ((ScoreRecordMapper) getMapper());
        return (Page<ScoreRecordPo>) mapper.selectVipScoreRecord(userId, getTimeBeforeYear(ONE));
    }
}
