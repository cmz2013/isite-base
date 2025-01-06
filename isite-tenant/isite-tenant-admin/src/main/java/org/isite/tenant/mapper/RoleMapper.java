package org.isite.tenant.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.RolePo;
import org.springframework.stereotype.Repository;

/**
 * @Description 角色信息DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface RoleMapper extends PoMapper<RolePo, Integer> {
}
