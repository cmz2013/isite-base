package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.mybatis.data.TreePo;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "department")
public class DepartmentPo extends TreePo<Integer> {
    /**
     * 租户ID
     */
    private Integer tenantId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 部门状态
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ActiveStatus status;
}
