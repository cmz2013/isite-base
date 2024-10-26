package org.isite.user.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.user.po.WechatPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface WechatMapper extends PoMapper<WechatPo, Long> {
}
