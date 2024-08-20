package org.isite.commons.lang;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class Regex {
    private static final Pattern MOBILE_PHONE = compile("^1[345789]\\d{9}$");

    /**
     * 判断是否为手机号
     */
    public static boolean isMobilePhone(String value){
        return MOBILE_PHONE.matcher(value).matches();
    }
}
