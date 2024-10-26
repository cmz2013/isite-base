package org.isite.user.converter;

import org.isite.user.data.dto.WechatPostDto;
import org.isite.user.po.WechatPo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class WechatConverter {

    private WechatConverter() {
    }

    public static WechatPo toWechatPo(WechatPostDto wechatPostDto, long userId) {
        WechatPo wechatPo = new WechatPo();
        wechatPo.setUserId(userId);
        wechatPo.setNickname(wechatPostDto.getUsername());
        wechatPo.setOpenId(wechatPostDto.getOpenId());
        return wechatPo;
    }
}
