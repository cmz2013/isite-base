package org.isite.operation.converter;

import org.isite.operation.data.dto.ActivityDto;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.enums.SwitchStatus.DISABLED;
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
            activityPo.setProperty(BLANK_STRING);
        }
        if (null == activityPo.getRemark()) {
            activityPo.setRemark(BLANK_STRING);
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
