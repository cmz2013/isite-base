package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;

/**
 * @Description 标签，主要用于用户画像
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TagDto extends Dto<Integer> {
    /**
     * 分类ID
     */
    private Integer categoryId;
    /**
     * 标签名称
     */
    private String title;
    /**
     * 标签描述
     */
    private String remark;
}
