package org.isite.tenant.service;

import org.isite.mybatis.service.PoService;
import org.isite.tenant.mapper.DataApiMapper;
import org.isite.tenant.po.DataApiPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DataApiService extends PoService<DataApiPo, Integer> {

    @Autowired
    public DataApiService(DataApiMapper dataApiMapper) {
        super(dataApiMapper);
    }
}
