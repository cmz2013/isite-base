package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;

/**
 * @Description 标签，主要用于用户画像
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Tag extends Vo<Integer> {
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
