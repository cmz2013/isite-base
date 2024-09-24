package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.TreeDto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotBlank;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TagCategoryDto extends TreeDto<Integer> {
    /**
     * 分类名称
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String title;
    /**
     * 分类描述
     */
    private String remark;
}
