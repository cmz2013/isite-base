package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Tree;
import org.isite.commons.lang.enums.SwitchStatus;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Department extends Tree<Department, Integer> {
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 部门状态
     */
    private SwitchStatus status;
    /**
     * 备注
     */
    private String remark;
}
