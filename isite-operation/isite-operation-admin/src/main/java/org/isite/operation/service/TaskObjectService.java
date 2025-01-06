package org.isite.operation.service;

import org.isite.mybatis.service.PoService;
import org.isite.operation.mapper.TaskObjectMapper;
import org.isite.operation.po.TaskObjectPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TaskObjectService extends PoService<TaskObjectPo, Long> {

    @Autowired
    public TaskObjectService(TaskObjectMapper taskObjectMapper) {
        super(taskObjectMapper);
    }
}
