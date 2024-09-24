package org.isite.commons.cloud.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Description 树结构 VO
 * @param <T> children泛型参数，可用于将JSON字符串解析成集合元素实例（否则Jackson不能解析children泛型参数）
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Tree<T extends Tree<T,  I>, I> extends Vo<I> {
    /**
     * 父节点ID（根节点为0）
     */
    private I pid;
    /**
     * 子节点。子节点和根节点类型必须相同
     */
    private List<T> children;

    /**
     * 根据pid是判断该节点是否为根节点
     */
    public boolean isRoot() {
        return null == pid || pid.equals(ZERO) || BLANK_STRING.equals(pid);
    }
}
