package org.isite.operation.converter;

import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.operation.converter.TaskConverter.toTasks;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class ActivityConverter {

    private ActivityConverter() {
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
