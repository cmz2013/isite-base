package org.isite.commons.web.exception;

import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ResultStatus;
import org.springframework.validation.BindingResult;
/**
 * @Description BindingResult辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class BindingResultAccessor {

    private BindingResultAccessor() {
    }

    public static String getMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        bindingResult.getAllErrors().forEach(e -> message.append(e.getDefaultMessage()).append(Constants.SEMICOLON));
        return message.length() > Constants.ZERO ?
                message.substring(Constants.ZERO, message.length() - Constants.ONE) :
                ResultStatus.EXPECTATION_FAILED.getReasonPhrase();
    }
}
