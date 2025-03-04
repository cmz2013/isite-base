package org.isite.user.service;

import org.isite.mybatis.service.PoService;
import org.isite.user.mapper.VipMapper;
import org.isite.user.po.VipPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
/**
 * @Description 用户信息Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class VipService extends PoService<VipPo, Long> {

    @Autowired
    public VipService(VipMapper vipMapper) {
        super(vipMapper);
    }

    public boolean isVip(long userId) {
        VipPo vipPo = this.findOne(VipPo::getUserId, userId);
        return null != vipPo && vipPo.getExpireTime().isAfter(LocalDateTime.now());
    }
}
