package org.isite.mybatis.service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@FunctionalInterface
public interface TreePidsGenerator {
    /**
     * 根据数据库类型使用不同的函数，将pids字段的前缀oldPids替换为newPids
     */
    String setPids(String newPids, String oldPids);
}
