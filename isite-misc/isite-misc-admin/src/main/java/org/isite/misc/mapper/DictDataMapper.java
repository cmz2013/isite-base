package org.isite.misc.mapper;

import org.isite.misc.po.DictDataPo;
import org.isite.mybatis.mapper.PoMapper;
import org.springframework.stereotype.Repository;
/**
 * @Description 字典数据DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DictDataMapper extends PoMapper<DictDataPo, Integer> {
}
