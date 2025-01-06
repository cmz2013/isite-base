package org.isite.commons.web.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.SEMICOLON;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.ResultStatus.EXPECTATION_FAILED;

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
