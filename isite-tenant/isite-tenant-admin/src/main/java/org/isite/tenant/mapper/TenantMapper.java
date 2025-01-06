package org.isite.tenant.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.TenantPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TenantMapper extends PoMapper<TenantPo, Integer> {

    List<TenantPo> selectByUserId(@Param("userId") long userId);
}
