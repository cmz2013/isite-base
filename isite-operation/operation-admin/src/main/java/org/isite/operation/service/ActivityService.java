package org.isite.operation.service;

import lombok.SneakyThrows;
import org.isite.commons.cloud.enums.TerminalType;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.mybatis.service.PoService;
import org.isite.operation.data.enums.EventType;
import org.isite.operation.mapper.ActivityMapper;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizeCodePo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.po.WebpagePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.lang.String.format;
import static org.isite.commons.cloud.enums.TerminalType.APP;
import static org.isite.commons.cloud.enums.TerminalType.WEB;
import static org.isite.commons.lang.enums.SwitchStatus.DISABLED;
import static org.isite.commons.lang.utils.IoUtils.getString;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Service
public class ActivityService extends PoService<ActivityPo, Integer> {

    private PrizeService prizeService;
    private PrizeCodeService prizeCodeService;
    private TaskService taskService;
    private WebpageService webpageService;

    @Autowired
    public ActivityService(ActivityMapper activityMapper) {
        super(activityMapper);
    }

    /**
     * 根据行为类型查询上架的运营活动ID
     */
    public List<Integer> findActivityIds(EventType eventType) {
        return ((ActivityMapper) getMapper()).selectActivityIds(eventType);
    }

    /**
     * 根据活动ID和上下架状态查询活动信息
     */
    public ActivityPo getActivity(int activityId, SwitchStatus status) {
        ActivityPo query = new ActivityPo();
        query.setId(activityId);
        query.setStatus(status);
        return findOne(query);
    }

    /**
     * 查询子活动
     * @param pid 主活动ID
     * @param status 活动上下架状态
     * @return 子活动列表
     */
    public List<ActivityPo> findActivities(Integer pid, SwitchStatus status) {
        ActivityPo activityPo = new ActivityPo();
        activityPo.setPid(pid);
        activityPo.setStatus(status);
        return findList(activityPo);
    }

    /**
     * 添加活动基本信息，初始化PC端和移动端活动页面模板
     * @param activityPo 活动基本信息
     * @return 活动ID
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public int addActivity(ActivityPo activityPo) {
        activityPo.setStatus(DISABLED);
        super.insert(activityPo);
        addWebpage(activityPo, WEB);
        addWebpage(activityPo, APP);
        return activityPo.getId();
    }

    private void addWebpage(ActivityPo activityPo, TerminalType terminalType) throws IOException {
        InputStream input = getClass().getResourceAsStream(
                format(terminalType.getViewPattern(), activityPo.getTheme().getWebpage()));
        if (null != input) {
            webpageService.insert(activityPo.getId(), terminalType, getString(input));
        }
    }

    /**
     * 更新活动上下架状态
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Integer activityId, SwitchStatus status) {
        ActivityPo activityPo = new ActivityPo();
        activityPo.setId(activityId);
        activityPo.setStatus(status);
        return updateSelectiveById(activityPo);
    }

    /**
     * 删除活动及其子活动
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteActivity(int activityId) {
        taskService.delete(TaskPo::getActivityId, activityId);
        prizeService.delete(PrizePo::getActivityId, activityId);
        prizeCodeService.delete(PrizeCodePo::getActivityId, activityId);
        webpageService.delete(WebpagePo::getActivityId, activityId);
        return delete(activityId);
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @Autowired
    public void setPrizeCodeService(PrizeCodeService prizeCodeService) {
        this.prizeCodeService = prizeCodeService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setWebpageService(WebpageService webpageService) {
        this.webpageService = webpageService;
    }
}
