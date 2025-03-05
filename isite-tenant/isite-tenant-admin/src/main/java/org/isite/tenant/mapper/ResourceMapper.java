package org.isite.tenant.mapper;

import org.isite.mybatis.mapper.TreePoMapper;
import org.isite.tenant.po.ResourcePo;
import org.springframework.stereotype.Repository;
/**
 * @Description 系统资源DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ResourceMapper extends TreePoMapper<ResourcePo, Integer> {
}
