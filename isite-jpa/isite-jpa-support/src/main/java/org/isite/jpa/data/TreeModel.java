package org.isite.jpa.data;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public interface TreeModel<I> extends Model<I> {
    /**
     * 获取父节点ID（根节点为0）
     */
    I getPid();

    /**
     * 获取从根节点到当前节点的父节点，所有ID有序逗号分隔。通过右模糊可以查询分支节点下的所有节点
     */
    String getPids();
}
