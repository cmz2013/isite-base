package org.isite.commons.lang;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class Regex {

    private Regex() {
    }

    /**
     * 数字正则表达式
     */
    private static final String DIGIT_PATTERN = "^[0-9]\\d*$";
    /**
     * 手机号正则表达式
     */
    private static final Pattern MOBILE_PHONE_PATTERN = compile("^1[345789]\\d{9}$");

    /**
     * 判断是否为数字
     */
    public static boolean isDigit(String value) {
        return DIGIT_PATTERN.matches(value);
    }

    /**
     * 判断是否为手机号
     */
    public static boolean isMobilePhone(String value) {
        return MOBILE_PHONE_PATTERN.matcher(value).matches();
    }
}
