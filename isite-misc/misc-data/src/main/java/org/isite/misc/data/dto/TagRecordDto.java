package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.misc.data.enums.ObjectType;

import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TagRecordDto extends Dto<Integer> {

    @NotNull(groups = {Add.class, Update.class})
    private Integer tagId;

    @NotNull(groups = {Add.class, Update.class})
    private ObjectType objectType;
    /**
     * 数据对象唯一标识（ID等）
     */
    @NotNull(groups = {Add.class, Update.class})
    private String objectValue;
}
