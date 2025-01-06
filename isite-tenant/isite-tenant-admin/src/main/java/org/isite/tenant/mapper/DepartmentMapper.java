package org.isite.tenant.mapper;

import org.isite.mybatis.mapper.TreePoMapper;
import org.isite.tenant.po.DepartmentPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DepartmentMapper extends TreePoMapper<DepartmentPo, Integer> {
}
