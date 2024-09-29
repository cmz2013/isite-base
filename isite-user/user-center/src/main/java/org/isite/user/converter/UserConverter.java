package org.isite.user.converter;

import org.isite.user.data.dto.UserPutDto;
import org.isite.user.data.dto.UserPostDto;
import org.isite.user.data.vo.UserDetails;
import org.isite.user.po.UserPo;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserConverter {

    private UserConverter() {
    }

    public static UserPo toUserPo(String phone) {
        UserPo userPo = new UserPo();
        userPo.setUserName(BLANK_STR);
        userPo.setPhone(phone);
        userPo.setRealName(BLANK_STR);
        userPo.setStatus(ENABLED);
        userPo.setInternal(FALSE);
        userPo.setRemark(BLANK_STR);
        userPo.setPassword(BLANK_STR);
        userPo.setEmail(BLANK_STR);
        return userPo;
    }

    public static UserDetails toUserDetails(UserPo userPo, boolean vip) {
        UserDetails userDetails = convert(userPo, UserDetails::new);
        userDetails.setVip(vip);
        return userDetails;
    }

    public static UserPo toUserPo(UserPostDto userPostDto) {
        UserPo userPo = convert(userPostDto, UserPo::new);
        userPo.setStatus(ENABLED);
        userPo.setInternal(FALSE);
        if (null == userPo.getEmail()) {
            userPo.setEmail(BLANK_STR);
        }
        userPo.setRemark(BLANK_STR);
        return userPo;
    }

    public static UserPo toUserSelectivePo(UserPutDto userPutDto) {
        UserPo userPo = convert(userPutDto, UserPo::new);
        if (null == userPo.getEmail()) {
            userPo.setEmail(BLANK_STR);
        }
        return userPo;
    }
}
