package org.isite.commons.web.exception;

import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ResultStatus;

import javax.validation.ConstraintViolation;
import java.util.Set;
/**
 * @Description ConstraintViolation辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class ConstraintViolationUtils {

    private ConstraintViolationUtils() {
    }

    public static String getMessage(Set<ConstraintViolation<?>> constraintViolations) {
        StringBuilder message = new StringBuilder();
        constraintViolations.forEach(e -> message.append(e.getMessage()).append(Constants.SEMICOLON));
        return message.length() > Constants.ZERO ?
                message.substring(Constants.ZERO, message.length() - Constants.ONE) :
                ResultStatus.EXPECTATION_FAILED.getReasonPhrase();
    }
}
