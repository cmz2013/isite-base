package org.isite.user.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.sign.Signature;
import org.isite.user.data.dto.UserDto;
import org.isite.user.data.vo.User;
import org.isite.user.data.vo.UserSecret;
import org.isite.user.po.UserPo;
import org.isite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.user.converter.UserConverter.toUserPo;
import static org.isite.user.converter.UserConverter.toUserSelectivePo;
import static org.isite.user.converter.UserSecretConverter.toSecret;
import static org.isite.user.data.constant.UrlConstants.API_GET_USER_SECRET;
import static org.isite.user.data.constant.UrlConstants.API_POST_USER;
import static org.isite.user.data.constant.UrlConstants.API_PUT_USER_PASSWORD;
import static org.isite.user.data.constant.UrlConstants.GET_USER;
import static org.isite.user.data.constant.UrlConstants.GET_USERS;
import static org.isite.user.data.constant.UrlConstants.URL_USER;

/**
 * @Description 用户Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class UserController extends BaseController {

    private static final String KEY_USERNAME_EXISTS = "userName.exists";
    private static final String KEY_PHONE_EXISTS = "phone.exists";
    private static final String KEY_EMAIL_EXISTS = "email.exists";
    private static final String VALUE_USERNAME_EXISTS = "username already exists";
    private static final String VALUE_PHONE_EXISTS = "phone number already exists";
    private static final String VALUE_EMAIL_EXISTS = "email already exists";

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(GET_USERS)
    public PageResult<User> findPage(PageRequest<UserDto> request) {
        try (Page<UserPo> page = userService.findPage(toPageQuery(request, UserPo::new))) {
            return toPageResult(request, convert(page.getResult(), User::new), page.getTotal());
        }
    }

    /**
     * 管理员查询用户信息
     */
    @GetMapping(GET_USER)
    public Result<User> getUser(@PathVariable("identifier") String identifier) {
        return toResult(convert(userService.getByIdentifier(identifier), User::new));
    }

    /**
     * 用于认证鉴权中心注册用户信息
     */
    @Signature
    @PostMapping(API_POST_USER)
    public Result<Integer> addUser(@RequestBody @Validated(Add.class) UserDto userDto) {
        isFalse(userService.exists(UserPo::getUserName, userDto.getUserName()), getMessage(KEY_USERNAME_EXISTS, VALUE_USERNAME_EXISTS));
        isFalse(userService.exists(UserPo::getPhone, userDto.getPhone()), getMessage(KEY_PHONE_EXISTS, VALUE_PHONE_EXISTS));
        if (isNotBlank(userDto.getEmail())) {
            isFalse(userService.exists(UserPo::getEmail, userDto.getEmail()), getMessage(KEY_EMAIL_EXISTS, VALUE_EMAIL_EXISTS));
        }
        return toResult(userService.insert(toUserPo(userDto)));
    }

    @PutMapping(URL_USER)
    public Result<Integer> updateUser(@RequestBody @Validated(Update.class) UserDto userDto) {
        if (isNotBlank(userDto.getUserName())) {
            isFalse(userService.exists(UserPo::getUserName, userDto.getUserName(), userDto.getId()), getMessage(KEY_USERNAME_EXISTS, VALUE_USERNAME_EXISTS));
        }
        if (isNotBlank(userDto.getPhone())) {
            isFalse(userService.exists(UserPo::getPhone, userDto.getPhone(), userDto.getId()), getMessage(KEY_PHONE_EXISTS, VALUE_PHONE_EXISTS));
        }
        if (isNotBlank(userDto.getEmail())) {
            isFalse(userService.exists(UserPo::getEmail, userDto.getEmail(), userDto.getId()), getMessage(KEY_EMAIL_EXISTS, VALUE_EMAIL_EXISTS));
        }
        return toResult(userService.updateSelectiveById(toUserSelectivePo(userDto)));
    }

    @Signature
    @GetMapping(API_GET_USER_SECRET)
    public Result<UserSecret> getUserSecret(@PathVariable("identifier") String identifier) {
        return toResult(toSecret(userService.getByIdentifier(identifier)));
    }

    /**
     * 用于认证鉴权中心更新用户密码
     */
    @Signature
    @PutMapping(API_PUT_USER_PASSWORD)
    public Result<Integer> updatePassword(
            @PathVariable("userId") Long userId, @RequestParam("password") String password) {
        return toResult(userService.updatePassword(userId, password));
    }
}
