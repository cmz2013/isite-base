package org.isite.tenant.mapper;

import org.isite.mybatis.mapper.TreePoMapper;
import org.isite.tenant.po.ResourcePo;
import org.springframework.stereotype.Repository;

/**
 * 系统资源DAO
 * @author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ResourceMapper extends TreePoMapper<ResourcePo, Integer> {
}
