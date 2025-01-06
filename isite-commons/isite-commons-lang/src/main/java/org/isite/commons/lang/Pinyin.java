package org.isite.commons.lang;

import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.pinyin4j.PinyinHelper.toHanyuPinyinStringArray;
import static net.sourceforge.pinyin4j.format.HanyuPinyinCaseType.LOWERCASE;
import static net.sourceforge.pinyin4j.format.HanyuPinyinToneType.WITH_TONE_MARK;
import static net.sourceforge.pinyin4j.format.HanyuPinyinVCharType.WITH_U_UNICODE;

/**
 * 汉语拼音工具类
 * @author <font color='blue'>zhangcm</font>
 */
public class Pinyin {
    private static final Map<String, String> CHINESE_CODES = new HashMap<>();
    private static final String CHINESE_REGEX_FORMAT = "[%s]+";

    static {
        CHINESE_CODES.put(UTF_8.name(), "\\u4e00-\\u9fa5");
    }

    private Pinyin() {
    }

    /**
     * 得到汉子的汉语拼音
     */
    public static String[] toPinyin(char c) throws BadHanyuPinyinOutputFormatCombination {
        return toPinyin(c, UTF_8);
    }

    /**
     * 得到汉子的汉语拼音
     */
    public static String[] toPinyin(char c, Charset charset) throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(LOWERCASE);
        format.setToneType(WITH_TONE_MARK);
        format.setVCharType(WITH_U_UNICODE);
        if (Character.toString(c).matches(format(CHINESE_REGEX_FORMAT, CHINESE_CODES.get(charset.name())))) {
            return toHanyuPinyinStringArray(c, format);
        }
        return null;
    }
}
