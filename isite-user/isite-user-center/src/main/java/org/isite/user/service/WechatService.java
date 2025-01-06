package org.isite.user.service;

import org.isite.mybatis.service.PoService;
import org.isite.user.mapper.WechatMapper;
import org.isite.user.po.WechatPo;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class WechatService extends PoService<WechatPo, Long> {

    public WechatService(WechatMapper mapper) {
        super(mapper);
    }
}
