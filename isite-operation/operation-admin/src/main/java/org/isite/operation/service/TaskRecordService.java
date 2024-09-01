package org.isite.operation.service;

import org.apache.ibatis.session.RowBounds;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.mybatis.service.PoService;
import org.isite.operation.po.TaskRecordPo;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.support.constants.OperationConstants.FIELD_ACTIVITY_ID;
import static org.isite.operation.support.constants.OperationConstants.FIELD_ACTIVITY_PID;
import static org.isite.user.data.constants.UserConstants.FIELD_USER_ID;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class TaskRecordService<P extends TaskRecordPo> extends PoService<P, Long> {

    protected TaskRecordService(PoMapper<P, Long> mapper) {
        super(mapper);
    }

    /**
     * @return 数据类
     */
    @Override
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(this.getClass(), TaskRecordService.class));
    }

    /**
     * 查询用户的任务记录，如果是主活动，任务记录包含了主活动及其子活动（两级）的任务记录
     */
    public List<P> findList(int activityId, long userId) {
        Weekend<P> weekend = of(getPoClass());
        Example example = new Example(getPoClass());
        weekend.and(example.createCriteria().andEqualTo(FIELD_USER_ID, userId));
        weekend.and(example.createCriteria().orEqualTo(FIELD_ACTIVITY_ID, activityId)
                .orEqualTo(FIELD_ACTIVITY_PID, activityId));
        return super.findList(weekend);
    }

    /**
     * 是否存在用户的任务记录。如果是主活动，包含主活动及其子活动（两级）的任务记录
     */
    public boolean exists(int activityId, long userId) {
        Weekend<P> weekend = of(getPoClass());
        Example example = new Example(getPoClass());
        weekend.and(example.createCriteria().andEqualTo(FIELD_USER_ID, userId));
        weekend.and(example.createCriteria().orEqualTo(FIELD_ACTIVITY_ID, activityId)
                .orEqualTo(FIELD_ACTIVITY_PID, activityId));
        return isNotEmpty(getMapper().selectByExampleAndRowBounds(
                weekend, new RowBounds(ZERO, ONE)));
    }

    /**
     * 是否存在任务记录。如果是主活动，包含主活动及其子活动（两级）的任务记录
     */
    public boolean exists(int activityId) {
        return isNotEmpty(getMapper().selectByExampleAndRowBounds(
                of(getPoClass()).weekendCriteria().orEqualTo(P::getActivityId, activityId)
                        .orEqualTo(P::getActivityPid, activityId),
                new RowBounds(ZERO, ONE)));
    }

}
