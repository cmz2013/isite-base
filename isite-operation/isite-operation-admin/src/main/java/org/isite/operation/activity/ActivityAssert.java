package org.isite.operation.activity;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.enums.ActiveStatus;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ActivityAssert {

    private ActivityAssert() {
    }

    public static void notOnline(ActiveStatus status) {
        Assert.isTrue(ActiveStatus.DISABLED.equals(status), MessageUtils.getMessage("activity.online",
                "the activity is online and cannot be operated"));
    }

    public static void notExistTaskRecord(boolean exists) {
        Assert.isFalse(exists, MessageUtils.getMessage("activity.taskRecord",
                "there are task records that cannot be operated"));
    }
}
