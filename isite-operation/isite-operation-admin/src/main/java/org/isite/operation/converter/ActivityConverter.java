package org.isite.operation.converter;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.commons.lang.json.Jackson;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.support.dto.ActivityDto;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ActivityConverter {

    private ActivityConverter() {
    }

    public static ActivityPo toActivityPo(ActivityDto activityDto) {
        ActivityPo activityPo = DataConverter.convert(activityDto, ActivityPo::new);
        activityPo.setStatus(ActiveStatus.DISABLED);
        if (null == activityPo.getPid()) {
            activityPo.setPid(Constants.ZERO);
        }
        if (null == activityPo.getProperty()) {
            activityPo.setProperty(Constants.BLANK_STR);
        }
        if (null == activityPo.getRemark()) {
            activityPo.setRemark(Constants.BLANK_STR);
        }
        return activityPo;
    }

    /**
     * to VO
     */
    public static Activity toActivity(ActivityPo activityPo, List<TaskPo> taskPos, List<PrizePo> prizePos) {
        Activity activity = new Activity();
        activity.setTasks(TaskConverter.toTasks(taskPos));
        if (StringUtils.isNotBlank(activityPo.getProperty())) {
            activity.setProperty(Jackson.parseObject(activityPo.getProperty(), activityPo.getTheme().getPropertyClass()));
        }
        activity.setTheme(activityPo.getTheme());
        activity.setPrizes(DataConverter.convert(prizePos, Prize::new));
        activity.setCreateTime(activityPo.getCreateTime());
        activity.setEndTime(activityPo.getEndTime());
        activity.setId(activityPo.getId());
        activity.setPid(activityPo.getPid());
        activity.setRemark(activityPo.getRemark());
        activity.setStatus(activityPo.getStatus());
        activity.setStartTime(activityPo.getStartTime());
        activity.setTitle(activityPo.getTitle());
        activity.setUpdateTime(activityPo.getUpdateTime());
        return activity;
    }
}
