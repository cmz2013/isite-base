package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.ResourceApiMapper;
import org.isite.tenant.po.DataApiPo;
import org.isite.tenant.po.ResourceApiPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ResourceApiService extends PoService<ResourceApiPo, Integer> {

    @Autowired
    public ResourceApiService(ResourceApiMapper mapper) {
        super(mapper);
    }

    /**
     * 根据资源ID查询数据接口
     */
    public List<DataApiPo> findDataApis(List<Integer> resourceIds) {
        return ((ResourceApiMapper) getMapper()).selectDataApis(resourceIds);
    }
}
