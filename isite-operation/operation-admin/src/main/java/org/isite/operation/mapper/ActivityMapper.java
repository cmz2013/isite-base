package org.isite.operation.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.data.enums.EventType;
import org.isite.operation.po.ActivityPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ActivityMapper extends PoMapper<ActivityPo, Integer> {
    /**
     * 根据行为类型查询上架的运营活动ID
     * @param eventType 行为类型
     * @return 活动ID
     */
    List<Integer> selectEnabledActivityIds(@Param("eventType") EventType eventType);

}
