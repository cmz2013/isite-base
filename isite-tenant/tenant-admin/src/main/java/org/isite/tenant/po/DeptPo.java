package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.mybatis.data.TreePo;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "dept")
public class DeptPo extends TreePo<Integer> {
    /**
     * 租户ID
     */
    private Integer tenantId;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 部门状态
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private SwitchStatus status;
}
