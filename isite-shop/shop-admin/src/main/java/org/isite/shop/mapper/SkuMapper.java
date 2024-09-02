package org.isite.shop.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.shop.po.SkuPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface SkuMapper extends PoMapper<SkuPo, Integer> {
}
