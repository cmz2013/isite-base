package org.isite.operation.converter;

import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.support.dto.ActivityDto;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.ActiveStatus.DISABLED;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.operation.converter.TaskConverter.toTasks;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ActivityConverter {

    private ActivityConverter() {
    }

    public static ActivityPo toActivityPo(ActivityDto activityDto) {
        ActivityPo activityPo = convert(activityDto, ActivityPo::new);
        activityPo.setStatus(DISABLED);
        if (null == activityPo.getPid()) {
            activityPo.setPid(ZERO);
        }
        if (null == activityPo.getProperty()) {
            activityPo.setProperty(BLANK_STR);
        }
        if (null == activityPo.getRemark()) {
            activityPo.setRemark(BLANK_STR);
        }
        return activityPo;
    }

    /**
     * to VO
     */
    public static Activity toActivity(ActivityPo activityPo, List<TaskPo> taskPos, List<PrizePo> prizePos) {
        Activity activity = new Activity();
        activity.setTasks(toTasks(taskPos));
        if (isNotBlank(activityPo.getProperty())) {
            activity.setProperty(parseObject(activityPo.getProperty(), activityPo.getTheme().getPropertyClass()));
        }
        activity.setTheme(activityPo.getTheme());
        activity.setPrizes(convert(prizePos, Prize::new));
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
