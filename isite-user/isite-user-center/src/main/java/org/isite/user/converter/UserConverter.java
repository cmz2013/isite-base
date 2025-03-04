package org.isite.user.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.user.data.dto.UserPostDto;
import org.isite.user.data.dto.UserPutDto;
import org.isite.user.data.enums.Sex;
import org.isite.user.data.vo.UserBasic;
import org.isite.user.data.vo.UserDetails;
import org.isite.user.data.vo.UserSecret;
import org.isite.user.po.UserPo;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserConverter {

    private UserConverter() {
    }

    public static UserPo toUserPo(String phone) {
        UserPo userPo = new UserPo();
        userPo.setUsername(Constants.BLANK_STR);
        userPo.setPhone(phone);
        userPo.setRealName(Constants.BLANK_STR);
        userPo.setStatus(ActiveStatus.ENABLED);
        userPo.setInternal(Boolean.FALSE);
        userPo.setRemark(Constants.BLANK_STR);
        userPo.setPassword(Constants.BLANK_STR);
        userPo.setEmail(Constants.BLANK_STR);
        return userPo;
    }

    public static UserDetails toUserDetails(UserPo userPo, boolean vip) {
        UserDetails userDetails = DataConverter.convert(userPo, UserDetails::new);
        userDetails.setVip(vip);
        return userDetails;
    }

    public static UserPo toUserPo(UserPostDto userPostDto) {
        UserPo userPo = DataConverter.convert(userPostDto, UserPo::new);
        userPo.setStatus(ActiveStatus.ENABLED);
        userPo.setInternal(Boolean.FALSE);
        if (null == userPo.getHeadImg()) {
            userPo.setHeadImg(Constants.BLANK_STR);
        }
        if (null == userPo.getSex()) {
            userPo.setSex(Sex.UNKNOWN);
        }
        if (null == userPo.getEmail()) {
            userPo.setEmail(Constants.BLANK_STR);
        }
        if (null == userPo.getPassword()) {
            userPo.setPassword(Constants.BLANK_STR);
        }
        userPo.setRemark(Constants.BLANK_STR);
        return userPo;
    }

    public static UserPo toUserSelectivePo(UserPutDto userPutDto) {
        UserPo userPo = DataConverter.convert(userPutDto, UserPo::new);
        if (null == userPo.getEmail()) {
            userPo.setEmail(Constants.BLANK_STR);
        }
        return userPo;
    }

    public static UserBasic toUserBasic(UserPo userPo) {
        if (null == userPo) {
            return null;
        }
        UserBasic userBasic = new UserBasic();
        userBasic.setId(userPo.getId());
        userBasic.setUsername(userPo.getUsername());
        userBasic.setHeadImg(userPo.getHeadImg());
        userBasic.setSex(userPo.getSex());
        userBasic.setPhone(userPo.getPhone());
        userBasic.setEmail(userPo.getEmail());
        userBasic.setInternal(userPo.getInternal());
        userBasic.setStatus(userPo.getStatus());
        return userBasic;
    }

    public static List<UserBasic> toUserBasics(List<UserPo> userPos) {
        if (CollectionUtils.isEmpty(userPos)) {
            return null;
        }
        return userPos.stream().map(UserConverter::toUserBasic).collect(Collectors.toList());
    }

    public static UserSecret toUserSecret(UserPo userPo) {
        if (null == userPo) {
            return null;
        }
        UserSecret userSecret = new UserSecret();
        userSecret.setId(userPo.getId());
        userSecret.setUsername(userPo.getUsername());
        userSecret.setHeadImg(userPo.getHeadImg());
        userSecret.setSex(userPo.getSex());
        userSecret.setPassword(userPo.getPassword());
        userSecret.setPhone(userPo.getPhone());
        userSecret.setEmail(userPo.getEmail());
        userSecret.setInternal(userPo.getInternal());
        userSecret.setStatus(userPo.getStatus());
        return userSecret;
    }
}
