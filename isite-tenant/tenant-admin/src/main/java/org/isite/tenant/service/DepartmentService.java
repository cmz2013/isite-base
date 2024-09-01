package org.isite.tenant.service;

import org.isite.mybatis.service.TreePoService;
import org.isite.tenant.mapper.DepartmentMapper;
import org.isite.tenant.po.DepartmentPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 部门Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DepartmentService extends TreePoService<DepartmentPo, Integer> {

    @Autowired
    public DepartmentService(DepartmentMapper departmentMapper) {
        super(departmentMapper);
    }
}
