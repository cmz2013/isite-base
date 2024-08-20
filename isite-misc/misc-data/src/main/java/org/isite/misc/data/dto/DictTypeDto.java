package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotNull;

/**
 * @Description 字典类型 DTO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DictTypeDto extends Dto<Integer> {
    /**
     * 类型名称
     */
    @NotNull(groups = {Add.class, Update.class})
    private String name;
    /**
     * 字典类型值
     */
    @NotNull(groups = {Add.class, Update.class})
    private String value;
    /**
     * 备注
     */
    private String remark;
}
