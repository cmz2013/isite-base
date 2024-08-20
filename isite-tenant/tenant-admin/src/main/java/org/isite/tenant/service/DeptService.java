package org.isite.tenant.service;

import org.isite.mybatis.service.TreePoService;
import org.isite.tenant.mapper.DeptMapper;
import org.isite.tenant.po.DeptPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 部门Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DeptService extends TreePoService<DeptPo, Integer> {

    @Autowired
    public DeptService(DeptMapper deptMapper) {
        super(deptMapper);
    }
}
