package org.isite.commons.web.sync;

/**
 * 锁定义
 * @author <font color='blue'>zhangcm</font>
 */
public @interface Lock {
    /**
     * 锁名称name支持占位符 ${}（FreeMarker模板格式），使用key表达式返回的数据替换占位符
     * 1）name从基本类型参数取值时，包括基本类型的集合或数组，使用占位符：${arg0}、${arg1} ... arg下标对应key表达式数组下标
     * 2）name从POJO、Map中取值：${field}， field为Map中的key或POJO的属性名。注意：如果field的值为数组或集合时，不会批量加锁
     */
    String name();
    /**
     * 从接口参数列表读取条件的SpEL表达式，true：加锁，false：不加锁
     */
    String condition() default "true";
    /**
     * 从接口参数列表读取数据的SpEL变量表达式：#parameterName
     * 1) key不配置时，name不处理占位符
     * 2) key返回数据为集合或数组时，根据锁name（FreeMarker模板）批量加锁
     * 3) key返回数据为基本类型时，依次替换锁name占位符：${arg0}、${arg1} ...
     * 4) key返回数据为引用类型时，替换锁name占位符：${对象属性名}；如果属性值为数组或集合时，不会批量加锁
     * 5）如果设置key表达式错误，不能读取到值时，将导致加锁失败
     */
    String[] keys() default {};
    /**
     * 可重入次数。加锁时，在KEY的末尾追加当前进入次数
     */
    int reentry() default Locksmith.DEFAULT_REENTRY;
}
