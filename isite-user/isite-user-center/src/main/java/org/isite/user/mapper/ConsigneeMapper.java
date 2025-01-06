package org.isite.user.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.user.po.ConsigneePo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ConsigneeMapper extends PoMapper<ConsigneePo, Long> {

    ConsigneePo selectOneConsignee(@Param("userId") long userId);
}
