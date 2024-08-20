package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.RoleResourceMapper;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RoleResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ResourcePo> findResources(String clientId, int roleId) {
        return ((RoleResourceMapper) getMapper()).selectResources(clientId, roleId);
    }
}
