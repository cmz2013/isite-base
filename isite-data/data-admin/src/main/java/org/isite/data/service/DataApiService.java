package org.isite.data.service;

import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.data.po.DataApiPo;
import org.isite.mongo.service.PoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 数据接口Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DataApiService extends PoService<DataApiPo, String> {
    /**
     * 启停接口
     */
    @Transactional(rollbackFor = Exception.class)
    public Long updateById(String id, SwitchStatus status) {
        DataApiPo dataApiPo = new DataApiPo();
        dataApiPo.setId(id);
        dataApiPo.setStatus(status);
        return this.updateSelectiveById(dataApiPo);
    }
}
