package org.isite.security.converter;

import org.isite.security.data.dto.UserPostDto;
import org.isite.security.data.vo.OauthUser;
import org.isite.user.data.dto.UserDto;

import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.lang.Constants.ONE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserConverter {
    private static final String USERNAME = "username";

    private UserConverter() {
    }

    /**
     * 用户信息序列化
     */
    public static Map<String, Object> toUserMap(OauthUser oauthUser) {
        Map<String, Object> data = new HashMap<>(ONE);
        data.put(USERNAME, oauthUser.getUsername());
        return data;
    }

    public static UserDto toUserDto(UserPostDto userPostDto) {
        UserDto userDto = new UserDto();
        userDto.setUserName(userPostDto.getUserName());
        userDto.setRealName(userPostDto.getRealName());
        userDto.setEmail(userPostDto.getEmail());
        userDto.setPhone(userPostDto.getPhone());
        return userDto;
    }
}
