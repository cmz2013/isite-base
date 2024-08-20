package org.isite.tenant.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.DataApiPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DataApiMapper extends PoMapper<DataApiPo, Integer> {
}
