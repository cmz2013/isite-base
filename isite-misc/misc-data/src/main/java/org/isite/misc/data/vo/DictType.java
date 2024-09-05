package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;

/**
 * @Description 字典类型DTO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DictType extends Vo<Integer> {
    /**
     * 类型名称
     */
    private String name;
    /**
     * 字典类型值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;
}
