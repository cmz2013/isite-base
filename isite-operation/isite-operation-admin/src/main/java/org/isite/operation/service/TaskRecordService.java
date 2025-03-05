package org.isite.operation.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.mybatis.service.PoService;
import org.isite.operation.po.TaskRecordPo;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.user.data.constants.UserConstants;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;
/**
 * @Description 任务记录Service父类
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
        return TypeUtils.cast(Reflection.getGenericParameter(this.getClass(), TaskRecordService.class));
    }

    /**
     * 查询用户的任务记录，如果是主活动，任务记录包含了主活动及其子活动（两级）的任务记录
     */
    public List<P> findList(int activityId, long userId) {
        Weekend<P> weekend = Weekend.of(getPoClass());
        Example example = new Example(getPoClass());
        weekend.and(example.createCriteria().andEqualTo(UserConstants.FIELD_USER_ID, userId));
        weekend.and(example.createCriteria().orEqualTo(OperationConstants.FIELD_ACTIVITY_ID, activityId)
                .orEqualTo(OperationConstants.FIELD_ACTIVITY_PID, activityId));
        return super.findList(weekend);
    }

    /**
     * 是否存在用户的任务记录。如果是主活动，包含主活动及其子活动（两级）的任务记录
     */
    public boolean exists(int activityId, long userId) {
        Weekend<P> weekend = Weekend.of(getPoClass());
        Example example = new Example(getPoClass());
        weekend.and(example.createCriteria().andEqualTo(UserConstants.FIELD_USER_ID, userId));
        weekend.and(example.createCriteria().orEqualTo(OperationConstants.FIELD_ACTIVITY_ID, activityId)
                .orEqualTo(OperationConstants.FIELD_ACTIVITY_PID, activityId));
        return CollectionUtils.isNotEmpty(getMapper().selectByExampleAndRowBounds(
                weekend, new RowBounds(Constants.ZERO, Constants.ONE)));
    }

    /**
     * 是否存在任务记录。如果是主活动，包含主活动及其子活动（两级）的任务记录
     */
    public boolean exists(int activityId) {
        return CollectionUtils.isNotEmpty(getMapper().selectByExampleAndRowBounds(
                Weekend.of(getPoClass()).weekendCriteria().orEqualTo(P::getActivityId, activityId)
                        .orEqualTo(P::getActivityPid, activityId),
                new RowBounds(Constants.ZERO, Constants.ONE)));
    }
}
