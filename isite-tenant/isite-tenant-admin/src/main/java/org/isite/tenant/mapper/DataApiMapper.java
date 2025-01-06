package org.isite.tenant.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.DataApiPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface DataApiMapper extends PoMapper<DataApiPo, Integer> {

    List<DataApiPo> selectDataApis(@Param("userId") long userId, @Param("serviceId") String serviceId);
}
