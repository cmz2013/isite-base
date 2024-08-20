package org.isite.commons.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;

/**
 * @Description 消息工具类，Bean加载完以后才可以使用
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class MessageUtils  {

    private MessageUtils() {
    }

    public static String getMessage(Throwable throwable) {
        String message = throwable.getMessage();
        if (isBlank(message)) {
            message = getMessage("serverError", "server error");
        }
        log.error(message, throwable);
        return message;
    }

    public static String getMessage(String code, String defaultMessage) {
        return getBean(MessageSourceAccessor.class).getMessage(code, defaultMessage);
    }
}