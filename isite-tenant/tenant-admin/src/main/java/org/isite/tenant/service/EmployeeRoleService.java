package org.isite.tenant.service;

import org.apache.ibatis.session.RowBounds;
import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.EmployeeRoleMapper;
import org.isite.tenant.po.EmployeeRolePo;
import org.isite.tenant.po.RolePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.ZERO;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class EmployeeRoleService extends PoService<EmployeeRolePo, Integer> {

    @Autowired
    public EmployeeRoleService(EmployeeRoleMapper employeeRoleMapper) {
        super(employeeRoleMapper);
    }

    public List<RolePo> findEmployeeRoles(long employeeId) {
        return ((EmployeeRoleMapper) getMapper()).selectRoles(employeeId);
    }

    public boolean hasAdminRole(int tenantId, int roleId, long excludeEmployeeId) {
        Weekend<EmployeeRolePo> weekend = of(EmployeeRolePo.class);
        weekend.weekendCriteria().andEqualTo(EmployeeRolePo::getTenantId, tenantId)
                .andEqualTo(EmployeeRolePo::getRoleId, roleId)
                .andNotEqualTo(EmployeeRolePo::getEmployeeId, excludeEmployeeId);
        return isNotEmpty(getMapper().selectByExampleAndRowBounds(
                weekend, new RowBounds(ZERO, ONE)));
    }
}
