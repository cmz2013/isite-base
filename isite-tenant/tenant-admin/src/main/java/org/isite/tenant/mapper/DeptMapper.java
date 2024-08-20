package org.isite.tenant.mapper;

import org.isite.mybatis.mapper.TreePoMapper;
import org.isite.tenant.po.DeptPo;
import org.springframework.stereotype.Repository;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DeptMapper extends TreePoMapper<DeptPo, Integer> {
}
