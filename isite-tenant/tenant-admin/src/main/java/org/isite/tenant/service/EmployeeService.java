package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.EmployeeMapper;
import org.isite.tenant.po.EmployeePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 员工Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class EmployeeService extends PoService<EmployeePo, Long> {

    @Autowired
    public EmployeeService(EmployeeMapper employeeMapper) {
        super(employeeMapper);
    }

    /**
     * 查询员工信息
     */
    public EmployeePo getEmployee(int tenantId, long userId) {
        EmployeePo query = new EmployeePo();
        query.setUserId(userId);
        query.setTenantId(tenantId);
        return findOne(query);
    }
}
