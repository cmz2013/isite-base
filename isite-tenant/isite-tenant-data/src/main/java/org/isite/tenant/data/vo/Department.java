package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Tree;
import org.isite.commons.lang.enums.ActiveStatus;

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
    private ActiveStatus status;
    /**
     * 备注
     */
    private String remark;
}
