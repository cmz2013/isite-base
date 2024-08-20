package org.isite.operation.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.po.SignLogPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface SignLogMapper extends PoMapper<SignLogPo, Long> {

    SignLogPo selectLastSignLog(@Param("userId") long userId);
}
