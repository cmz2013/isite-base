package org.isite.user.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.sign.Signed;
import org.isite.user.data.dto.UserDto;
import org.isite.user.data.vo.UserBasic;
import org.isite.user.data.vo.UserDetails;
import org.isite.user.data.vo.UserSecret;
import org.isite.user.po.UserPo;
import org.isite.user.service.UserService;
import org.isite.user.service.VipService;
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
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.user.converter.UserConverter.toUserDetails;
import static org.isite.user.converter.UserConverter.toUserPo;
import static org.isite.user.converter.UserConverter.toUserSelectivePo;
import static org.isite.user.converter.UserSecretConverter.toUserSecret;
import static org.isite.user.data.constants.UrlConstants.API_GET_USER_SECRET;
import static org.isite.user.data.constants.UrlConstants.API_POST_USER;
import static org.isite.user.data.constants.UrlConstants.API_PUT_USER_PASSWORD;
import static org.isite.user.data.constants.UrlConstants.GET_USERS;
import static org.isite.user.data.constants.UrlConstants.GET_USER_DETAILS;
import static org.isite.user.data.constants.UrlConstants.POST_USER_IF_ABSENT;
import static org.isite.user.data.constants.UrlConstants.URL_USER;

/**
 * @Description 用户Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class UserController extends BaseController {

    private VipService vipService;
    private UserService userService;

    @Autowired
    public void setVipService(VipService vipService) {
        this.vipService = vipService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(GET_USERS)
    public PageResult<UserBasic> findPage(PageRequest<UserDto> request) {
        try (Page<UserPo> page = userService.findPage(toPageQuery(request, UserPo::new))) {
            return toPageResult(request, convert(page.getResult(), UserBasic::new), page.getTotal());
        }
    }

    /**
     * @Description 根据唯一标识（username、phone、email）获取用户信息
     */
    @GetMapping(GET_USER_DETAILS)
    public Result<UserDetails> getUser(@PathVariable("identifier") String identifier) {
        UserPo userPo = userService.getByIdentifier(identifier);
        notNull(userPo, getMessage("user.not.found", "user not found"));
        return toResult(toUserDetails(userPo, vipService.isVip(userPo.getId())));
    }

    /**
     * 用于认证鉴权中心注册用户信息
     */
    @Signed
    @PostMapping(API_POST_USER)
    public Result<Integer> addUser(@RequestBody @Validated(Add.class) UserDto userDto) {
        isFalse(userService.exists(UserPo::getUserName, userDto.getUserName()),
                getMessage("userName.exists",  "username already exists"));
        isFalse(userService.exists(UserPo::getPhone, userDto.getPhone()),
                getMessage("phone.exists", "phone number already exists"));
        if (isNotBlank(userDto.getEmail())) {
            isFalse(userService.exists(UserPo::getEmail, userDto.getEmail()),
                    getMessage("email.exists", "email already exists"));
        }
        return toResult(userService.insert(toUserPo(userDto)));
    }

    @PostMapping(POST_USER_IF_ABSENT)
    public Result<Long> addUserIfAbsent(@PathVariable("phone") String phone) {
        UserPo userPo = userService.getByIdentifier(phone);
        if (userPo == null) {
            userPo = toUserPo(phone);
            userService.insert(userPo);
        }
        return toResult(userPo.getId());
    }

    @PutMapping(URL_USER)
    public Result<Integer> updateUser(@RequestBody @Validated(Update.class) UserDto userDto) {
        isFalse(userService.exists(UserPo::getUserName, userDto.getUserName(), userDto.getId()),
                getMessage("userName.exists",  "username already exists"));
        isFalse(userService.exists(UserPo::getPhone, userDto.getPhone(), userDto.getId()),
                getMessage("phone.exists", "phone number already exists"));
        if (isNotBlank(userDto.getEmail())) {
            isFalse(userService.exists(UserPo::getEmail, userDto.getEmail(), userDto.getId()),
                    getMessage("email.exists", "email already exists"));
        }
        return toResult(userService.updateSelectiveById(toUserSelectivePo(userDto)));
    }

    @Signed
    @GetMapping(API_GET_USER_SECRET)
    public Result<UserSecret> getUserSecret(@PathVariable("identifier") String identifier) {
        return toResult(toUserSecret(userService.getByIdentifier(identifier)));
    }

    /**
     * 通过认证鉴权中心不登录更新用户密码
     */
    @Signed
    @PutMapping(API_PUT_USER_PASSWORD)
    public Result<Integer> updatePassword(
            @PathVariable("userId") Long userId, @RequestParam("password") String password) {
        return toResult(userService.updatePassword(userId, password));
    }
}
