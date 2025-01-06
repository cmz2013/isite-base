package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DictDataDto extends Dto<Integer> {
    /**
     * 字典类型
     */
    @NotNull(groups = {Add.class, Update.class})
    private String type;
    /**
     * 字典标签
     */
    @NotNull(groups = {Add.class, Update.class})
    private String label;
    /**
     * 字典值
     */
    @NotNull(groups = {Add.class, Update.class})
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
