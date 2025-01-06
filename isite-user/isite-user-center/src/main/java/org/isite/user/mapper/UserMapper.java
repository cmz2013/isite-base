package org.isite.user.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.user.po.UserPo;
import org.springframework.stereotype.Repository;

/**
 * @Description 用户信息DAO
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface UserMapper extends PoMapper<UserPo, Long> {
}
