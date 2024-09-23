package org.isite.commons.web.sync;

import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.COLON;
import static org.isite.commons.lang.Constants.ONE;

/**
 * @Description 锁注解
 * @Author <font color='blue'>zhangcm</font>
 */
public @interface Lock {
    /**
     * 锁的名称。name用于keys的前缀，name和keys至少要有一个不为空。
     */
    String name() default BLANK_STRING;
    /**
     * 从接口参数列表读取条件的SpEL表达式，true：加锁，false：不加锁
     */
    String condition() default "true";
    /**
     * 锁的键。使用SpEL变量表达式从接口参数列表读取数据生成动态键：#parameterName，SpEL表达式使用#来引用上下文中的变量。
     */
    String[] keys() default {};
    /**
     * 可重入次数。加锁时，在KEY的末尾追加当前进入次数
     */
    int reentry() default ONE;
    /**
     * name和keys的分隔符
     */
    String delimiter() default COLON;
}
