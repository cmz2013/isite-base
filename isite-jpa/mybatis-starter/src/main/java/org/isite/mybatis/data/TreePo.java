package org.isite.mybatis.data;

import lombok.Getter;
import lombok.Setter;
import org.isite.jpa.data.TreeModel;

/**
 * @Description 树节点PO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TreePo<I> extends Po<I> implements TreeModel<I> {
    /**
     * 父节点ID（根节点为0）
     */
    private I pid;
    /**
     * 从根节点到当前节点的父节点，所有ID有序逗号分隔。通过右模糊可以查询分支节点下的所有节点
     */
    private String pids;
}
