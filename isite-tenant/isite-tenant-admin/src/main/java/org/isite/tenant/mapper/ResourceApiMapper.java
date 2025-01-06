package org.isite.tenant.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.tenant.po.DataApiPo;
import org.isite.tenant.po.ResourceApiPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ResourceApiMapper extends PoMapper<ResourceApiPo, Integer> {

    List<DataApiPo> selectDataApis(@Param("resourceIds") List<Integer> resourceIds);
}
