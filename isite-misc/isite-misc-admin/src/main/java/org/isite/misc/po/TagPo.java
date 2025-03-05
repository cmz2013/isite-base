package org.isite.misc.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;
/**
 * @Description 标签，主要用于用户画像
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "tag")
public class TagPo extends Po<Integer> {
    /**
     * 标签名称
     */
    private String title;
    /**
     * 分类ID
     */
    private Integer categoryId;
    /**
     * 标签描述
     */
    private String remark;
}
