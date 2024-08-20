package org.isite.commons.web.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.SEMICOLON;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.http.HttpStatus.EXPECTATION_FAILED;

/**
 * ConstraintViolation辅助类
 * @author <font color='blue'>zhangcm</font>
 */
public class ConstraintViolationUtils {

    private ConstraintViolationUtils() {
    }

    public static String getMessage(Set<ConstraintViolation<?>> constraintViolations) {
        StringBuilder message = new StringBuilder();
        constraintViolations.forEach(e -> message.append(e.getMessage()).append(SEMICOLON));
        return message.length() > ZERO ?
                message.substring(ZERO, message.length() - ONE) :
                EXPECTATION_FAILED.getReasonPhrase();
    }
}
