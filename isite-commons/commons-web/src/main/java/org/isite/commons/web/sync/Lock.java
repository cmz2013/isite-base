package org.isite.commons.web.sync;

import static org.isite.commons.lang.Constants.ONE;

/**
 * @Description 锁注解
 * @Author <font color='blue'>zhangcm</font>
 */
public @interface Lock {
    /**
     * 锁名称name支持占位符 ${}（FreeMarker模板字符串），占位符会被keys表达式返回的数据替换。
     * 1）name从接口参数POJO、Map中取值时， 使用Map中的key或POJO的属性名：${parameterName.field}
     * 2）name从基本类型的接口参数取值时，则直接使用参数名：${parameterName}
     */
    String name();
    /**
     * 从接口参数列表读取条件的SpEL表达式，true：加锁，false：不加锁
     */
    String condition() default "true";
    /**
     * 从接口参数列表读取数据的SpEL变量表达式：#parameterName，SpEL表达式使用#来引用上下文中的变量。
     * 1) key不配置时，name不处理占位符
     * 2) key返回数据不能为为集合或数组，否则会导致加锁失败
     * 3）如果设置key表达式错误，不能读取到值时，将导致加锁失败
     */
    String[] keys() default {};
    /**
     * 可重入次数。加锁时，在KEY的末尾追加当前进入次数
     */
    int reentry() default ONE;
}
