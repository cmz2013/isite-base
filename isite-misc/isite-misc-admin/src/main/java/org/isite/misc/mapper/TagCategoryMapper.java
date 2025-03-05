package org.isite.misc.mapper;

import org.isite.misc.po.TagCategoryPo;
import org.isite.mybatis.mapper.TreePoMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description 标签分类DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TagCategoryMapper extends TreePoMapper<TagCategoryPo, Integer> {
}
