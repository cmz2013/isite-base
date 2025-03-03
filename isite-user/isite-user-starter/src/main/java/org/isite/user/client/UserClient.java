package org.isite.user.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.feign.SignInterceptor;
import org.isite.user.data.constants.UserUrls;
import org.isite.user.data.dto.UserPostDto;
import org.isite.user.data.dto.WechatPostDto;
import org.isite.user.data.vo.UserDetails;
import org.isite.user.data.vo.UserSecret;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "userClient", value = SERVICE_ID)
public interface UserClient {
    /**
     * @Description 根据唯一标识（id、username、phone）获取用户信息
     */
    @GetMapping(value = UserUrls.GET_USER_DETAILS)
    Result<UserDetails> getUserDetails(@PathVariable("identifier") String identifier);
    /**
     * @Description 查询用户秘钥
     */
    @GetMapping(value = UserUrls.API_GET_USER_SECRET)
    Result<UserSecret> getUserSecret(@PathVariable("identifier") String identifier,
                                     @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);
    /**
     * @Description 使用微信openId查询用户信息
     */
    @PostMapping(value = UserUrls.API_POST_USER_WECHAT)
    Result<Long> addWechat(@RequestBody WechatPostDto wechatPostDto,
                           @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);
    /**
     * @Description (用于认证鉴权中心)更新/设置用户密码，密码已加密
     */
    @PutMapping(value = UserUrls.API_PUT_USER_PASSWORD)
    Result<Integer> updatePassword(@PathVariable("userId") Long userId,
                                   @RequestParam("password") String userPassword,
                                   @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);

    /**
     * @Description (用于认证鉴权中心)注册用户信息
     */
    @PostMapping(UserUrls.API_POST_USER)
    Result<Long> addUser(@RequestBody UserPostDto userPostDto,
                         @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);

    @PostMapping(UserUrls.POST_USER_PHONE_IF_ABSENT)
    Result<Long> addPhoneIfAbsent(@PathVariable("phone") String phone);
}
