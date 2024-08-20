package org.isite.misc.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.TreePo;

import javax.persistence.Table;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "tag_category")
public class TagCategoryPo extends TreePo<Integer> {
    /**
     * 分类名称
     */
    private String title;
    /**
     * 分类描述
     */
    private String remark;
}
