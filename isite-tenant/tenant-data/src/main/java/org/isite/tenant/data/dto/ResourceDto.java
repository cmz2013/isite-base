package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.TreeDto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.tenant.data.enums.ResourceType;

import javax.validation.constraints.NotBlank;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ResourceDto extends TreeDto<Integer> {
    /**
     * 资源名称
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String name;
    /**
     * 链接
     */
    private String href;
    /**
     * 图标
     */
    private String icon;
    /**
     * 资源类型
     */
    @NotBlank(groups = {Add.class, Update.class})
    private ResourceType type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 终端ID
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String clientId;
    /**
     * 备注
     */
    private String remark;
}
