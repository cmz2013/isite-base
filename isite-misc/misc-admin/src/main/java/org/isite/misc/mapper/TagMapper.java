package org.isite.misc.mapper;

import org.isite.misc.po.TagPo;
import org.isite.mybatis.mapper.PoMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description 标签DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TagMapper extends PoMapper<TagPo, Integer> {
}
