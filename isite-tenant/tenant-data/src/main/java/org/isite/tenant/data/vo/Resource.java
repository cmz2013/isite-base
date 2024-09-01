package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Tree;
import org.isite.tenant.data.enums.ResourceType;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Resource extends Tree<Resource, Integer> {
    /**
     * 资源类型
     */
    private ResourceType type;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 链接
     */
    private String href;
    /**
     * 图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
}
