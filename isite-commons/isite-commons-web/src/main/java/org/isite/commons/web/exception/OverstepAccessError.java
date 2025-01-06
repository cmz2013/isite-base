package org.isite.commons.web.exception;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;

import static org.isite.commons.lang.enums.ResultStatus.FORBIDDEN;

/**
 * @Description 越权访问异常
 * @Author <font color='blue'>zhangcm</font>
 */
public class OverstepAccessError extends Error {

    public OverstepAccessError() {
        super(FORBIDDEN.getCode(), MessageUtils.getMessage("overstepAccess",
                "You accessed a resource that you don't have permission to"));
    }
}