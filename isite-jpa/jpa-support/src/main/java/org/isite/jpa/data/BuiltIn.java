package org.isite.jpa.data;

/**
 * @Description 系统内置数据，只能使用数据库脚本插入和删除，内置字段不能编辑（其他字段可以修改）
 * @Author <font color='blue'>zhangcm</font>
 */
public interface BuiltIn {

    Boolean getInternal();

    void setInternal(Boolean internal);
}
