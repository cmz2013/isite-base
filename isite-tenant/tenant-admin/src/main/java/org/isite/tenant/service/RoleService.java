package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.RoleMapper;
import org.isite.tenant.po.RolePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 角色信息Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class RoleService extends PoService<RolePo, Integer> {

    @Autowired
    public RoleService(RoleMapper roleMapper) {
        super(roleMapper);
    }
}
