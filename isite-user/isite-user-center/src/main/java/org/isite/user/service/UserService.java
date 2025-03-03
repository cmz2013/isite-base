package org.isite.user.service;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Regex;
import org.isite.mybatis.service.PoService;
import org.isite.user.converter.UserConverter;
import org.isite.user.converter.WechatConverter;
import org.isite.user.data.dto.UserPostDto;
import org.isite.user.data.dto.WechatPostDto;
import org.isite.user.mapper.UserMapper;
import org.isite.user.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
/**
 * @Description 用户信息Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class UserService extends PoService<UserPo, Long> {

    private WechatService wechatService;

    @Autowired
    public UserService(UserMapper userMapper) {
        super(userMapper);
    }

    /**
     * 更新用户密码
     */
    @Transactional(rollbackFor = Exception.class)
    public int updatePassword(Long userId, String password) {
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setPassword(password);
        return updateSelectiveById(userPo);
    }

    /**
     * 根据唯一标识（id、username、phone）查询用户信息
     */
    public UserPo getByIdentifier(String identifier) {
        Weekend<UserPo> weekend = Weekend.of(getPoClass());
        WeekendCriteria<UserPo, Object> criteria = weekend.weekendCriteria()
                .orEqualTo(UserPo::getUsername, identifier).orEqualTo(UserPo::getPhone, identifier);
        if (Regex.isDigit(identifier)) {
            criteria.orEqualTo(UserPo::getId, Long.parseLong(identifier));
        }
        return getMapper().selectOneByExample(weekend);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addUser(UserPostDto userPostDto) {
        Assert.isFalse(this.exists(UserPo::getUsername, userPostDto.getUsername()),
                MessageUtils.getMessage("username.exists",  "username already exists"));
        Assert.isFalse(this.exists(UserPo::getPhone, userPostDto.getPhone()),
                MessageUtils.getMessage("phone.exists", "phone number already exists"));
        if (StringUtils.isNotBlank(userPostDto.getEmail())) {
            Assert.isFalse(this.exists(UserPo::getEmail, userPostDto.getEmail()),
                    MessageUtils.getMessage("email.exists", "email already exists"));
        }
        UserPo userPo = UserConverter.toUserPo(userPostDto);
        this.insert(userPo);
        return userPo.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addWechat(WechatPostDto wechatPostDto) {
        long userId = this.addUser(wechatPostDto);
        wechatService.insert(WechatConverter.toWechatPo(wechatPostDto, userId));
        return userId;
    }

    @Autowired
    public void setWechatService(WechatService wechatService) {
        this.wechatService = wechatService;
    }
}
