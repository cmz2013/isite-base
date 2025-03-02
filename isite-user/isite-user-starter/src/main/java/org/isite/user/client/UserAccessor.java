package org.isite.user.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.user.data.constants.UserConstants;
import org.isite.user.data.dto.UserPostDto;
import org.isite.user.data.dto.WechatPostDto;
import org.isite.user.data.vo.UserDetails;
import org.isite.user.data.vo.UserSecret;

/**
 * @Description UserClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserAccessor {

    private UserAccessor() {
    }

    /**
     * @Description 根据唯一标识（id、username、phone）获取用户信息
     */
    public static UserDetails getUserDetails(Object identifier) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        UserClient userClient = feignClientFactory.getFeignClient(UserClient.class, UserConstants.SERVICE_ID);
        return ResultUtils.getData(userClient.getUserDetails(identifier.toString()));
    }

    /**
     * @Description 查询用户秘钥
     */
    public static UserSecret getUserSecret(String identifier, String signPassword) {
        UserClient userClient = ApplicationContextUtils.getBean(FeignClientFactory.class)
                .getFeignClient(UserClient.class, UserConstants.SERVICE_ID);
        return ResultUtils.getData(userClient.getUserSecret(identifier, signPassword));
    }

    /**
     * @Description (用于认证鉴权中心)更新/设置用户密码，密码已加密
     */
    public static Integer updatePassword(long userId, String userPassword, String signPassword) {
        UserClient userClient = ApplicationContextUtils.getBean(FeignClientFactory.class)
                .getFeignClient(UserClient.class, UserConstants.SERVICE_ID);
        return ResultUtils.getData(userClient.updatePassword(userId, userPassword, signPassword));
    }

    /**
     * @Description (用于认证鉴权中心)注册用户信息
     */
    public static Long addUser(UserPostDto userPostDto, String signPassword) {
        UserClient userClient = ApplicationContextUtils.getBean(FeignClientFactory.class)
                .getFeignClient(UserClient.class, UserConstants.SERVICE_ID);
        return ResultUtils.getData(userClient.addUser(userPostDto, signPassword));
    }

    /**
     * @Description 注册微信用户信息
     */
    public static Long addWechat(WechatPostDto wechatPostDto, String signPassword) {
        UserClient userClient = ApplicationContextUtils.getBean(FeignClientFactory.class)
                .getFeignClient(UserClient.class, UserConstants.SERVICE_ID);
        return ResultUtils.getData(userClient.addWechat(wechatPostDto, signPassword));
    }

    /**
     * @Description 如果用户不存在，则注册用户信息
     * @param phone 手机号
     * @return 用户ID
     */
    public static long addPhoneIfAbsent(String phone) {
        UserClient userClient = ApplicationContextUtils.getBean(FeignClientFactory.class)
                .getFeignClient(UserClient.class, UserConstants.SERVICE_ID);
        return ResultUtils.getData(userClient.addPhoneIfAbsent(phone));
    }
}
