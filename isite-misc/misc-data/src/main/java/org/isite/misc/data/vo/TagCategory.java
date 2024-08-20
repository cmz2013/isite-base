package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Tree;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TagCategory extends Tree<TagCategory, Integer> {
    /**
     * 分类名称
     */
    private String title;
    /**
     * 分类描述
     */
    private String remark;
}
