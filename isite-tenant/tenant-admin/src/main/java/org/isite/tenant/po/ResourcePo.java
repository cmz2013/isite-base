package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.TreePo;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.tenant.data.enums.ResourceType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * 供租户使用的资源PO。租户资源由管理员基于RBAC进行分配
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "resource")
public class ResourcePo extends TreePo<Integer> {
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 访问路径
     */
    private String href;
    /**
     * 排序方式
     */
    private Integer sort;
    /**
     * 资源类型（folder, menu, btn）
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ResourceType type;
    /**
     * 图标
     */
    private String icon;
    /**
     * 终端ID
     */
    private String clientId;
    /**
     * 备注
     */
    private String remark;
}
