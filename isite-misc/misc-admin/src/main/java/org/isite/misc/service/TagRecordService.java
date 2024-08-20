package org.isite.misc.service;

import org.isite.misc.mapper.TagRecordMapper;
import org.isite.misc.po.TagRecordPo;
import org.isite.mybatis.service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TagRecordService extends PoService<TagRecordPo, Integer> {

    @Autowired
    public TagRecordService(TagRecordMapper tagRecordMapper) {
        super(tagRecordMapper);
    }
}
