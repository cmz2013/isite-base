package org.isite.misc.mapper;

import org.isite.misc.po.DictTypePo;
import org.isite.mybatis.mapper.PoMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description 字典类型DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DictTypeMapper extends PoMapper<DictTypePo, Integer> {
}
