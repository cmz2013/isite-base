package org.isite.operation.service;

import org.isite.mybatis.service.PoService;
import org.isite.operation.activity.ActivityInitializer;
import org.isite.operation.activity.ActivityInitializerFactory;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.mapper.InitLogMapper;
import org.isite.operation.po.InitLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class InitLogService extends PoService<InitLogPo, Long> {

    private ActivityInitializerFactory activityInitializerFactory;

    public InitLogService(InitLogMapper mapper) {
        super(mapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveInitLog(Activity activity, long userId) {
        ActivityInitializer activityInitializer = activityInitializerFactory.get(activity.getTheme());
        if (null == activityInitializer) {
            return;
        }
        InitLogPo initLogPo = new InitLogPo(userId, activity.getId());
        if (null == findOne(initLogPo)) {
            this.insert(initLogPo);
            activityInitializer.execute(activity, userId);
        }
    }

    @Autowired
    public void setActivityInitializerFactory(ActivityInitializerFactory activityInitializerFactory) {
        this.activityInitializerFactory = activityInitializerFactory;
    }
}
