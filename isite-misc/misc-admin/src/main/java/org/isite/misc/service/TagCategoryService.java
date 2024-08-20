package org.isite.misc.service;

import org.isite.misc.po.TagCategoryPo;
import org.isite.mybatis.service.TreePoService;
import org.isite.misc.mapper.TagCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TagCategoryService extends TreePoService<TagCategoryPo, Integer> {

    @Autowired
    public TagCategoryService(TagCategoryMapper tagCategoryMapper) {
        super(tagCategoryMapper);
    }
}
