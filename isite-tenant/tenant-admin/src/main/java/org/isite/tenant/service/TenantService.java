package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.TenantMapper;
import org.isite.tenant.po.TenantPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TenantService extends PoService<TenantPo, Integer> {

    @Autowired
    public TenantService(TenantMapper tenantMapper) {
        super(tenantMapper);
    }

    public List<TenantPo> findByUserId(long userId) {
        return ((TenantMapper) getMapper()).selectByUserId(userId);
    }
}
