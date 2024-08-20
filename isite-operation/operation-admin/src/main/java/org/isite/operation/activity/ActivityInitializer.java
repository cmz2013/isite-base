package org.isite.operation.activity;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.operation.data.enums.ActivityTheme;
import org.isite.operation.data.vo.Activity;

/**
 * @Description 运营活动初始化接口（可以不实现该接口）
 * @Author <font color='blue'>zhangcm</font>
 */
public interface ActivityInitializer extends Strategy<ActivityTheme> {
    /**
     * 参加活动之前对用户数据进行预处理
     * @param activity 活动
     * @param userId 用户ID
     */
    void execute(Activity activity, long userId);
}
