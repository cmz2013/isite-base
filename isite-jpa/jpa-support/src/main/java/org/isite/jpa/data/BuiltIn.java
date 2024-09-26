package org.isite.jpa.data;

/**
 * @Description 系统内置数据模型父接口。系统内置数据，只能手动执行数据库脚本插入和删除，
 * 不能使用接口新增和删除，接口不能更新内置字段（其他字段可以修改）
 * @Author <font color='blue'>zhangcm</font>
 */
public interface BuiltIn {

    Boolean getInternal();

    void setInternal(Boolean internal);
}
