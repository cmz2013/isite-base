package org.isite.misc.mapper;

import org.isite.misc.po.TagRecordPo;
import org.isite.mybatis.mapper.PoMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description 标签记录DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TagRecordMapper extends PoMapper<TagRecordPo, Integer> {
}
