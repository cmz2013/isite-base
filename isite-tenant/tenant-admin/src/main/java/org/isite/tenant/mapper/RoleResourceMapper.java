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

    List<Integer> selectResourceIds(@Param("roleId") int roleId);

    /**
     * 删除该租户所有角色在role_resource表中对应的资源
     */
    void deleteRoleResources(@Param("tenantId") int tenantId, @Param("resourceIds") List<Integer> resourceIds);
}
