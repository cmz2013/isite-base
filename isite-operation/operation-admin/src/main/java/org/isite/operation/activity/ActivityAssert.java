package org.isite.operation.activity;

import org.isite.commons.lang.enums.SwitchStatus;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.enums.SwitchStatus.DISABLED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ActivityAssert {

    private ActivityAssert() {
    }

    public static void notOnline(SwitchStatus status) {
        isTrue(DISABLED.equals(status), getMessage("activity.online",
                "the activity is online and cannot be operated"));
    }

    public static void notExistTaskRecord(boolean exists) {
        isFalse(exists, getMessage("activity.taskRecord",
                "there are task records that cannot be operated"));
    }
}
