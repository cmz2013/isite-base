package org.isite.shop.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.shop.po.SkuPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface SkuMapper extends PoMapper<SkuPo, Integer> {

    int updateSoldNum(@Param("skuId") Integer skuId, @Param("skuNum") Integer skuNum);
}
