package org.isite.commons.lang;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description 汉语拼音工具类
 * @Author <font color='blue'>zhangcm</font>
 */
public class Pinyin {
    private static final Map<String, String> CHINESE_CODES = new HashMap<>();
    private static final String CHINESE_REGEX_FORMAT = "[%s]+";

    static {
        CHINESE_CODES.put(StandardCharsets.UTF_8.name(), "\\u4e00-\\u9fa5");
    }

    private Pinyin() {
    }

    /**
     * 得到汉子的汉语拼音
     */
    public static String[] toPinyin(char c) throws BadHanyuPinyinOutputFormatCombination {
        return toPinyin(c, StandardCharsets.UTF_8);
    }

    /**
     * 得到汉子的汉语拼音
     */
    public static String[] toPinyin(char c, Charset charset) throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        if (Character.toString(c).matches(String.format(CHINESE_REGEX_FORMAT, CHINESE_CODES.get(charset.name())))) {
            return PinyinHelper.toHanyuPinyinStringArray(c, format);
        }
        return null;
    }
}
