package org.isite.tenant.service;

import org.isite.mybatis.service.TreePoService;
import org.isite.tenant.mapper.ResourceMapper;
import org.isite.tenant.po.ResourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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
     * 根据客户端ID和pid查询资源
     */
    public List<ResourcePo> findResources(Integer pid, List<Integer> resourceIds) {
        Weekend<ResourcePo> weekend = of(ResourcePo.class);
        weekend.orderBy(ResourcePo::getSort);
        weekend.weekendCriteria().andEqualTo(ResourcePo::getPid, pid);
        if (isNotEmpty(resourceIds)) {
            weekend.weekendCriteria().andIn(ResourcePo::getId, resourceIds);
        }
        return findList(weekend);
    }
}
