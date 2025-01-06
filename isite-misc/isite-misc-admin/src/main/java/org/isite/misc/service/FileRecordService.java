package org.isite.misc.service;

import org.isite.misc.mapper.FileRecordMapper;
import org.isite.misc.po.FileRecordPo;
import org.isite.mybatis.service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class FileRecordService extends PoService<FileRecordPo, Integer> {

    @Autowired
    public FileRecordService(FileRecordMapper fileRecordMapper) {
        super(fileRecordMapper);
    }
}
