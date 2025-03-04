package org.isite.commons.cloud.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.data.vo.Tree;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TreeUtils {

    private TreeUtils() {
    }

    /**
     * 根据ID递归查询Tree列表
     */
    public static <T extends Tree<T, I>, I> T get(I id, List<T> trees) {
        if (CollectionUtils.isEmpty(trees)) {
            return null;
        }
        for (T tree : trees) {
            if (tree.getId().equals(id)) {
                return tree;
            }
            tree = get(id, tree.getChildren());
            if (null != tree) {
                return tree;
            }
        }
        return null;
    }

    /**
     * 将树source合并到target，如果根节点不同不能合并
     */
    public static <T extends Tree<T, I>, I> void merge(T source, T target) {
        if (!target.getId().equals(source.getId()) || CollectionUtils.isEmpty(source.getChildren())) {
            return;
        }
        if (CollectionUtils.isEmpty(target.getChildren())) {
            target.setChildren(source.getChildren());
        } else {
            source.getChildren().forEach(child -> {
                T node = get(child.getId(), target.getChildren());
                if (null == node) {
                    target.getChildren().add(child);
                } else {
                    merge(child, node);
                }
            });
        }
    }
}
