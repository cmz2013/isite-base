package org.isite.operation.service;

import org.isite.operation.mapper.InviteRecordMapper;
import org.isite.operation.po.InviteRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Service
public class InviteRecordService extends TaskRecordService<InviteRecordPo> {

    @Autowired
    public InviteRecordService(InviteRecordMapper mapper) {
        super(mapper);
    }
}
