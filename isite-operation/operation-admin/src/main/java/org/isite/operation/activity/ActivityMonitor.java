package org.isite.operation.activity;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.operation.support.enums.ActivityTheme;
import org.isite.operation.support.vo.Activity;

/**
 * @Description 运营活动监视接口（可以不实现该接口）
 * @Author <font color='blue'>zhangcm</font>
 */
public interface ActivityMonitor extends Strategy<ActivityTheme> {
    /**
     * 该用户是否可以执行任务
     * @param activity 活动
     * @param userId 用户ID
     * @return true：允许，false：禁止
     */
    boolean participate(Activity activity, long userId);
}
