package org.isite.tenant.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.EmployeeRolePo;
import org.isite.tenant.po.RolePo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface EmployeeRoleMapper extends PoMapper<EmployeeRolePo, Integer> {

    List<RolePo> selectRoles(@Param("employeeId") long employeeId);
}
