package org.isite.security.converter;

import org.isite.security.data.dto.UserRegistDto;
import org.isite.security.data.vo.OauthUser;
import org.isite.tenant.data.vo.Role;
import org.isite.user.data.dto.UserPostDto;
import org.isite.user.data.vo.UserBasic;
import org.isite.user.data.vo.UserSecret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.isite.commons.lang.Constants.TWO;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;
import static org.isite.security.converter.ResourceConverter.toResourceMaps;
import static org.isite.security.converter.ResourceConverter.toResources;
import static org.isite.security.converter.RoleConverter.toRoleMaps;
import static org.isite.security.converter.TenantConverter.toTenantMap;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserConverter {

    private static final String USERNAME = "username";
    private static final String ROLES = "roles";
    private static final String RESOURCES = "resources";
    private static final String TENANT = "tenant";

    private UserConverter() {
    }

    public static UserPostDto toUserPostDto(UserRegistDto userRegistDto) {
        UserPostDto userPostDto = new UserPostDto();
        userPostDto.setUsername(userRegistDto.getUsername());
        userPostDto.setRealName(userRegistDto.getRealName());
        userPostDto.setHeadImg(userRegistDto.getHeadImg());
        userPostDto.setSex(userRegistDto.getSex());
        userPostDto.setEmail(userRegistDto.getEmail());
        userPostDto.setPhone(userRegistDto.getPhone());
        userPostDto.setPassword(userRegistDto.getPassword());
        return userPostDto;
    }

    /**
     * 员工信息序列化
     */
    public static Map<String, Object> toUserMap(
            OauthUser oauthUser, List<Role> roles, boolean hasRole) {
        Map<String, Object> data = new HashMap<>(TWO);
        data.put(USERNAME, oauthUser.getUsername());
        data.put(TENANT, toTenantMap(oauthUser.getTenant()));
        if (isNotTrue(hasRole)) {
            data.put(RESOURCES, toResourceMaps(toResources(roles)));
        } else {
            data.put(ROLES, toRoleMaps(roles));
        }
        return data;
    }

    public static OauthUser toOauthUser(long userId, UserPostDto userPostDto) {
        OauthUser user = new OauthUser();
        user.setUserId(userId);
        user.setUsername(userPostDto.getUsername());
        user.setHeadImg(userPostDto.getHeadImg());
        user.setSex(userPostDto.getSex());
        user.setInternal(FALSE);
        user.setEnabled(TRUE);
        return user;
    }

    public static OauthUser toOauthUser(UserBasic userBasic) {
        OauthUser user = new OauthUser();
        user.setUserId(userBasic.getId());
        user.setUsername(userBasic.getUsername());
        user.setHeadImg(userBasic.getHeadImg());
        user.setSex(userBasic.getSex());
        user.setInternal(userBasic.getInternal());
        user.setEnabled(ENABLED.equals(userBasic.getStatus()));
        return user;
    }

    public static OauthUser toOauthUser(UserSecret userSecret) {
        if (null == userSecret) {
            return null;
        }
        OauthUser user = toOauthUser((UserBasic) userSecret);
        user.setPassword(userSecret.getPassword());
        return user;
    }
}