package org.isite.commons.web.sync;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;
import org.springframework.http.HttpStatus;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ConcurrentError extends Error {

    public ConcurrentError() {
        super(HttpStatus.CONFLICT.value(), MessageUtils.getMessage("concurrencyError", "concurrency error"));
    }
}
