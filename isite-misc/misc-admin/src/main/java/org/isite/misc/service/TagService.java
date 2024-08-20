package org.isite.misc.service;

import org.isite.misc.mapper.TagMapper;
import org.isite.misc.po.TagPo;
import org.isite.mybatis.service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TagService extends PoService<TagPo, Integer> {

    @Autowired
    public TagService(TagMapper tagMapper) {
        super(tagMapper);
    }
}
