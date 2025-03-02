package org.isite.commons.cloud.converter;

import org.isite.commons.cloud.data.vo.Tree;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Constants;
import org.isite.jpa.data.TreeModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.cloud.utils.TreeUtils.get;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TreeConverter {

    private TreeConverter() {
    }

    /**
     * 树节点PO转成完整路径的Tree
     * @param nodes 全部树节点PO列表
     * @param converter 树节点PO转VO
     * @param <T> 树节点，Tree子类
     * @param <P> 树节点PO
     * @param <I> 主键
     */
    public static <T extends Tree<T, I>, P extends TreeModel<I>, I> List<T>
    toTree(List<P> nodes, Function<P, T> converter) {
        return toTree(nodes, converter, null);
    }

    /**
     * 树节点PO转成完整路径的Tree
     * @param nodes 部分树节点PO列表
     * @param converter 树节点PO转VO
     * @param query 如果父节点不在nodes列表中，query根据ID查询父节点；query为空时则不查询父节点。
     * @param <T> 树节点，Tree子类（DTO/BO）
     * @param <P> 树节点PO
     * @param <I> ID（主键）
     */
    public static <T extends Tree<T, I>, P extends TreeModel<I>, I> List<T>
    toTree(List<P> nodes, Function<P, T> converter, Function<Set<I>, List<P>> query) {
        if (isEmpty(nodes)) {
            return emptyList();
        }
        //树节点结果集，在链表中进行移除操作
        List<T> trees = new LinkedList<>();
        //转为链表，在链表中进行移除操作，不修改入参数据
        nodes = new LinkedList<>(nodes);
        Set<I> pids = new HashSet<>();

        //按顺序遍历PO链表（树节点PO列表有可能排过序），从PO列表移除转为T，依次添加到结果集中。
        for (int i = Constants.ZERO; i < nodes.size();) {
            toTree(toTree(nodes.remove(i), converter), trees, nodes, converter, pids);
        }
        return toTree(trees, pids, converter, query);
    }

    /**
     * 添加树节点到结果集中
     */
    private static <T extends Tree<T, I>, P extends TreeModel<I>, I>
    void toTree(T tree, List<T> trees, List<P> nodes, Function<P, T> converter, Set<I> pids) {
        //根节点
        if (tree.isRoot()) {
            if (!VoUtils.contain(trees, tree.getId())) {
                trees.add(tree);
            }
            return;
        }
        T parent = toTree(tree.getPid(), trees, nodes, converter);
        if (null != parent) {
            parent.getChildren().add(tree);
            //递归查询和添加parent节点的父节点
            toTree(parent, trees, nodes, converter, pids);
        } else {
            //结果集中非根节点的父节点ID，用于批量查询
            pids.add(tree.getPid());
            //将非根节点临时添加到结果集中
            trees.add(tree);
        }
    }

    /**
     * 创建T实例
     */
    private static <T extends Tree<T, I>, P extends TreeModel<I>, I> T toTree(P node, Function<P, T> converter) {
        T tree = converter.apply(node);
        if (null == tree.getChildren()) {
            tree.setChildren(new ArrayList<>());
        }
        return tree;
    }

    /**
     * 查询结果集中非根节点的父节点，组装树
     */
    private static <T extends Tree<T, I>, P extends TreeModel<I>, I>
    List<T> toTree(List<T> trees, Set<I> pids, Function<P, T> converter, Function<Set<I>, List<P>> query) {
        if (null == query || isEmpty(pids)) {
            return trees;
        }
        //批量查询结果集中非根节点的父节点PO
        List<P> nodes = new LinkedList<>(query.apply(pids));
        pids.clear();

        //按顺序遍历PO链表（树节点PO列表排过序），从PO列表移除转为T，依次添加到结果集中。
        for (int i = Constants.ZERO; i < nodes.size();) {
            T parent = toTree(nodes.remove(i), converter);
            Iterator<T> iterator = trees.iterator();
            while (iterator.hasNext()) {
                T tree = iterator.next();
                if (parent.getId().equals(tree.getPid())) {
                    //将非根节点tree添加到父节点的children集合中，并将非根节点tree从结果集中移除
                    parent.getChildren().add(tree);
                    iterator.remove();
                }
            }
            //将parent添加到结果集中
            toTree(parent, trees, nodes, converter, pids);
        }
        //递归查询结果集中非根节点的父节点
        return toTree(trees, pids, converter, query);
    }

    /**
     * 转换树节点
     * 1、从结果集（trees）中查询树节点
     * 2、如果未查询到，则从树节点PO列表查询和转换树节点
     * @param id 树节点ID
     * @param trees 结果集
     * @param nodes 树节点PO列表
     * @param converter 树节点PO转VO方法
     */
    private static <T extends Tree<T, I>, P extends TreeModel<I>, I>
    T toTree(I id, List<T> trees, List<P> nodes, Function<P, T> converter) {
        T tree = get(id, trees);
        if (null == tree) {
            //从PO列表中查询和移除父节点，并转为T
            return remove(nodes, id, converter);
        }
        return tree;
    }

    /**
     * 从PO列表中查询和移除节点，并转为T
     */
    private static <T extends Tree<T, I>, P extends TreeModel<I>, I>
    T remove(List<P> nodes, I id, Function<P, T> converter) {
        for (P node : nodes) {
            if (id.equals(node.getId())) {
                //从po列表移除父节点。增强型for循环，第一次删除完毕后使用return跳出，如果继续循环会抛出并发修改异常
                nodes.remove(node);
                return toTree(node, converter);
            }
        }
        return null;
    }
}
