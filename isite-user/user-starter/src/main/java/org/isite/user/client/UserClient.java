package org.isite.user.client;

import org.isite.commons.cloud.data.Result;
import org.isite.user.data.dto.UserDto;
import org.isite.user.data.vo.UserDetails;
import org.isite.user.data.vo.UserSecret;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import static org.isite.commons.cloud.feign.SignInterceptor.FEIGN_SIGN_PASSWORD;
import static org.isite.user.data.constants.UrlConstants.API_GET_USER_SECRET;
import static org.isite.user.data.constants.UrlConstants.API_POST_USER;
import static org.isite.user.data.constants.UrlConstants.API_PUT_USER_PASSWORD;
import static org.isite.user.data.constants.UrlConstants.GET_USER_BY_IDENTIFIER;
import static org.isite.user.data.constants.UrlConstants.POST_USER_IF_ABSENT;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "userClient", value = SERVICE_ID)
public interface UserClient {
    /**
     * @Description 根据唯一标识（id、username、phone）获取用户信息
     */
    @GetMapping(value = GET_USER_BY_IDENTIFIER)
    Result<UserDetails> getUserDetails(@PathVariable("identifier") String identifier);
    /**
     * @Description 查询用户秘钥
     */
    @GetMapping(value = API_GET_USER_SECRET)
    Result<UserSecret> getUserSecret(@PathVariable("identifier") String identifier,
                                     @RequestHeader(FEIGN_SIGN_PASSWORD) String signPassword);
    /**
     * @Description (用于认证鉴权中心)更新/设置用户密码，密码已加密
     */
    @PutMapping(value = API_PUT_USER_PASSWORD)
    Result<Integer> updatePassword(@PathVariable("userId") Long userId,
                                   @RequestParam("password") String userPassword,
                                   @RequestHeader(FEIGN_SIGN_PASSWORD) String signPassword);

    /**
     * @Description (用于认证鉴权中心)注册用户信息
     */
    @PostMapping(API_POST_USER)
    Result<Integer> addUser(@RequestBody UserDto userDto,
                            @RequestHeader(FEIGN_SIGN_PASSWORD) String signPassword);

    @PostMapping(POST_USER_IF_ABSENT)
    Result<Long> addUserIfAbsent(@PathVariable("phone") String phone);
}
