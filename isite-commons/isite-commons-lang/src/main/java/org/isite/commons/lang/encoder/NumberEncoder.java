package org.isite.commons.lang.encoder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description 自然数（正整数）编码解码操作
 * 应用场景举例：分享链接如果需要携带用户ID，可通过编码保护ID
 * @Author <font color='blue'>zhangcm</font>
 */
public class NumberEncoder {

    private NumberEncoder() {
    }

    /**
     * 用于编码的原始字符列表，不能有重复字符
     */
    private static final char[] CHAR_LIST = new char[] {
            'R', 'T', 'g', 'h', '0', '7', 'W', 'X', '3', 'o', '9', 'Y', 'F', 'L', '2', 'n', 'H', 'r', 'S', '5', 'x',
            'N', 'B', '1', 'a', '8', 'e', 'f', 'E', 'J', '4', 'K', 'y', 'Q', 'P', 'U', 'A', 'b', 'd', 'l', 'Z', 'w',
            'I', 't', 'G', 'z', 'M', 'u', 'c', 'v', 'C', 'i', 'j', 'D', 'V', 'k', 'm', 'p', '6', 'q', 's', 'O'
    };

    /**
     * @Description 自然数（正整数）编码算法：
     * 1）从列表中取第一个编码因子
     * 2）使用编码因子将原始字符列表（CHAR_LIST）一分为二，生成新的62个字符列表：第二个列表（编码因子对应字符作为首字母） + 第一个列表
     * 3）根据列表顺序，取下一个编码因子，使用2）新产生的62个字符列表重复 2）操作
     * 4）按顺序处理完所有编码因子，使用最终生成的62个字符列表对自然数进行编码。
     * @param number 正整数
     * @param passwords 秘钥
     */
    public static String encode(long number, int... passwords) {
        if (number < Constants.ZERO) {
            throw new IllegalArgumentException(String.valueOf(number));
        }
        char[] charList = getCharList(passwords);
        StringBuilder code = new StringBuilder();
        do {
            code.append(charList[(int) (number % charList.length)]);
            number = number / charList.length;
        } while (number != Constants.ZERO);
        return code.toString();
    }

    /**
     * @Description 使用密码重组字符列表
     */
    private static char[] getCharList(int[] passwords) {
        char[] charList = CHAR_LIST;
        for (int password : passwords) {
            charList = getCharList(charList, password);
        }
        return charList;
    }

    /**
     * @Description 使用密码重组字符列表
     */
    private static char[] getCharList(char[] srcList, int password) {
        if (Constants.ZERO == password) {
            return srcList;
        }
        if (password >= CHAR_LIST.length) {
            password = password % CHAR_LIST.length;
        }
        return ArrayUtils.addAll(Arrays.copyOfRange(srcList, password, CHAR_LIST.length),
                Arrays.copyOfRange(CHAR_LIST, Constants.ZERO, password));
    }

    /**
     * @Description 获取用于解码（编码）的字符列表
     */
    private static Map<Character, Integer> getCharMap(int... passwords) {
        char[] charList = getCharList(passwords);
        Map<Character, Integer> charMap = new HashMap<>();
        for(int i = Constants.ZERO; i < charList.length; i++) {
            charMap.put(charList[i], i);
        }
        return charMap;
    }

    /**
     * @Description 自然数（正整数）解码
     */
    public static long decode(String code, int... passwords) {
        long result = Constants.ZERO;
        Map<Character, Integer> charMap = getCharMap(passwords);
        code = StringUtils.reverse(code);
        for(int i = Constants.ZERO; i < code.length(); ++i) {
            Integer index = charMap.get(code.charAt(i));
            if (index == null) {
                throw new IllegalArgumentException(code);
            }
            result = result * charMap.size() + index;
        }
        return result;
    }
}
