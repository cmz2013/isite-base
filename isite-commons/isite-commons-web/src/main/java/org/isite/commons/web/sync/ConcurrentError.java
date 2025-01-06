package org.isite.commons.web.sync;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Error;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ConcurrentError extends Error {

    public ConcurrentError() {
        super(CONFLICT.value(), MessageUtils.getMessage("concurrencyError", "concurrency error"));
    }
}
