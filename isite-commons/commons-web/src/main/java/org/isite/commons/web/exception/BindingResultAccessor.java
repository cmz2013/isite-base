package org.isite.commons.web.exception;

import org.springframework.validation.BindingResult;

import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.SEMICOLON;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.http.HttpStatus.EXPECTATION_FAILED;

/**
 * BindingResult辅助类
 * @author <font color='blue'>zhangcm</font>
 */
public class BindingResultAccessor {

    private BindingResultAccessor() {
    }

    public static String getMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        bindingResult.getAllErrors().forEach(e -> message.append(e.getDefaultMessage()).append(SEMICOLON));
        return message.length() > ZERO ?
                message.substring(ZERO, message.length() - ONE) :
                EXPECTATION_FAILED.getReasonPhrase();
    }
}
