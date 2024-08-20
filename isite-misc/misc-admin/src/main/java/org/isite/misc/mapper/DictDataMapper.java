package org.isite.misc.mapper;

import org.isite.misc.po.DictDataPo;
import org.isite.mybatis.mapper.PoMapper;
import org.springframework.stereotype.Repository;

/**
 * 字典数据DAO
 * @author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DictDataMapper extends PoMapper<DictDataPo, Integer> {
}
