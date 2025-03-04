package org.isite.tenant.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.RoleResourceMapper;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RoleResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class RoleResourceService extends PoService<RoleResourcePo, Integer> {

    @Autowired
    public RoleResourceService(RoleResourceMapper roleResourceMapper) {
        super(roleResourceMapper);
    }

    /**
     * 根据角色ID查询终端资源
     */
    public List<ResourcePo> findRoleResources(String clientId, int roleId) {
        return ((RoleResourceMapper) getMapper()).selectResources(clientId, roleId);
    }

    /**
     * 根据角色ID查询资源ID
     */
    public List<Integer> findRoleResourceIds(int roleId) {
        return ((RoleResourceMapper) getMapper()).selectResourceIds(roleId);
    }

    /**
     * 删除该租户所有角色在role_resource表中对应的资源
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleResources(int tenantId, List<Integer> resourceIds) {
        if (CollectionUtils.isEmpty(resourceIds)) {
            return;
        }
        ((RoleResourceMapper) getMapper()).deleteRoleResources(tenantId, resourceIds);
    }
}
