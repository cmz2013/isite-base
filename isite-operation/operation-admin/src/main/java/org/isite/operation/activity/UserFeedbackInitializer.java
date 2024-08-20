package org.isite.operation.activity;

import org.isite.operation.data.enums.ActivityTheme;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.service.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.isite.operation.data.enums.ActivityTheme.USER_FEEDBACK;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserFeedbackInitializer implements ActivityInitializer {
    private UserFeedbackService userFeedbackService;

    @Async
    @Override
    public void execute(Activity activity, long userId) {
        userFeedbackService.grantPrize(activity, userId);
    }

    @Autowired
    public void setUserFeedbackService(UserFeedbackService userFeedbackService) {
        this.userFeedbackService = userFeedbackService;
    }

    @Override
    public ActivityTheme[] getIdentities() {
        return new ActivityTheme[] {USER_FEEDBACK};
    }
}
