package org.isite.commons.lang.data;

/**
 * @Description 系统内置数据，只能使用数据库脚本插入，内置字段不能编辑
 * @Author <font color='blue'>zhangcm</font>
 */
public interface BuiltIn {

    Boolean getInternal();

    void setInternal(Boolean internal);
}
