package org.isite.tenant.service;

import org.isite.mybatis.service.TreePoService;
import org.isite.tenant.mapper.ResourceMapper;
import org.isite.tenant.po.ResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Description 系统资源Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ResourceService extends TreePoService<ResourcePo, Integer> {

    @Autowired
    public ResourceService(ResourceMapper resourceMapper) {
        super(resourceMapper);
    }

    /**
     * 根据pid查询终端资源
     */
    public List<ResourcePo> findResources(String clientId, Integer pid) {
        Weekend<ResourcePo> weekend = of(ResourcePo.class);
        weekend.orderBy(ResourcePo::getSort);
        weekend.weekendCriteria().andEqualTo(ResourcePo::getPid, pid)
                .andEqualTo(ResourcePo::getClientId, clientId);
        return findList(weekend);
    }
}
