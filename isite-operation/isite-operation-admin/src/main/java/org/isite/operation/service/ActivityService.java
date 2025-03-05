package org.isite.operation.service;

import lombok.SneakyThrows;
import org.isite.commons.cloud.data.enums.TerminalType;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.commons.lang.utils.IoUtils;
import org.isite.mybatis.service.PoService;
import org.isite.operation.mapper.ActivityMapper;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizeCodePo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.po.WebpagePo;
import org.isite.operation.support.enums.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
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
    public List<Integer> findEnabledActivityIds(EventType eventType) {
        return ((ActivityMapper) getMapper()).selectEnabledActivityIds(eventType);
    }

    /**
     * 根据pid是判断该节点是否为根节点
     */
    public boolean isRoot(Integer pid) {
        return null == pid || pid.equals(Constants.ZERO);
    }

    /**
     * 根据活动ID和上下架状态查询活动信息
     */
    public ActivityPo getActivity(int activityId, ActiveStatus status) {
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
    public List<ActivityPo> findActivities(Integer pid, ActiveStatus status) {
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
        super.insert(activityPo);
        addWebpage(activityPo, TerminalType.WEB);
        addWebpage(activityPo, TerminalType.APP);
        return activityPo.getId();
    }

    private void addWebpage(ActivityPo activityPo, TerminalType terminalType) throws IOException {
        InputStream input = getClass().getResourceAsStream(String.format(
                terminalType.getViewPattern(), activityPo.getTheme().getWebpage()));
        if (null != input) {
            webpageService.insert(activityPo.getId(), terminalType, IoUtils.getString(input));
        }
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
