package org.isite.user.service;

import org.isite.mybatis.service.PoService;
import org.isite.user.mapper.UserMapper;
import org.isite.user.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;

import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Description 用户信息Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class UserService extends PoService<UserPo, Long> {

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
     * 根据唯一标识（username、phone、email）查询用户信息
     */
    public UserPo getByIdentifier(String identifier) {
        Weekend<UserPo> weekend = of(getPoClass());
        weekend.weekendCriteria().orEqualTo(UserPo::getUserName, identifier)
                .orEqualTo(UserPo::getPhone, identifier)
                .orEqualTo(UserPo::getEmail, identifier);
        return getMapper().selectOneByExample(weekend);
    }
}
