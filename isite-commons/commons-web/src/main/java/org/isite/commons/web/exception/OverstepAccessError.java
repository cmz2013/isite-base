package org.isite.commons.web.exception;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;

import static org.isite.commons.lang.http.HttpStatus.FORBIDDEN;

/**
 * @Description 越权访问异常
 * @Author <font color='blue'>zhangcm</font>
 */
public class OverstepAccessError extends Error {

    public OverstepAccessError() {
        super(FORBIDDEN.getCode(), MessageUtils.getMessage("overstepAccess", "You do not have access to this resource"));
    }
}
