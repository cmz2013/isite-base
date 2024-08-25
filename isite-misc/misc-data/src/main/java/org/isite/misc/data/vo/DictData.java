package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DictData extends Vo<Integer> {
    /**
     * 字典类型
     */
    private String type;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
}
