package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.RoleMapper;
import org.isite.tenant.po.RolePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.tenant.data.constant.TenantConstants.ROLE_ADMINISTRATOR;

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

    /**
     * @Description 租户不可以修改或删除 Administrator 角色，一个租户有且只有一个 Administrator 角色
     */
    @Transactional(rollbackFor = Exception.class)
    public int addAdminRole(int tenantId) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(ROLE_ADMINISTRATOR);
        rolePo.setRemark(BLANK_STRING);
        this.insert(rolePo);
        return rolePo.getId();
    }

    public RolePo getAdminRole(int tenantId) {
        RolePo rolePo = new RolePo();
        rolePo.setTenantId(tenantId);
        rolePo.setName(ROLE_ADMINISTRATOR);
        return this.findOne(rolePo);
    }
}
