package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.EmployeeRoleMapper;
import org.isite.tenant.po.EmployeeRolePo;
import org.isite.tenant.po.RolePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class EmployeeRoleService extends PoService<EmployeeRolePo, Integer> {

    @Autowired
    public EmployeeRoleService(EmployeeRoleMapper employeeRoleMapper) {
        super(employeeRoleMapper);
    }

    public List<RolePo> findRoles(long employeeId) {
        return ((EmployeeRoleMapper) getMapper()).selectRoles(employeeId);
    }
}
