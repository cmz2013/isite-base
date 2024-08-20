package org.isite.tenant.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RoleResourcePo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface RoleResourceMapper extends PoMapper<RoleResourcePo, Integer> {

    List<ResourcePo> selectResources(@Param("clientId") String clientId, @Param("roleId") int roleId);
}
