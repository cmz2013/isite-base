package org.isite.user.service;

import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.mybatis.service.PoService;
import org.isite.user.mapper.ConsigneeMapper;
import org.isite.user.po.ConsigneePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.isite.commons.lang.Assert.isTrue;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ConsigneeService extends PoService<ConsigneePo, Long> {

    @Autowired
    public ConsigneeService(ConsigneeMapper consigneeMapper) {
        super(consigneeMapper);
    }

    /**
     * 设置默认地址（最多只能有一个）
     */
    @Transactional(rollbackFor = Exception.class)
    public int setDefaults(long userId, long consigneeId) {
        isTrue(get(consigneeId).getUserId().equals(userId), new OverstepAccessError());
        clearDefaults(userId);
        ConsigneePo consigneePo = new ConsigneePo();
        consigneePo.setId(consigneeId);
        consigneePo.setDefaults(TRUE);
        return updateSelectiveById(consigneePo);
    }

    private void clearDefaults(long userId) {
        ConsigneePo consigneePo = new ConsigneePo();
        consigneePo.setDefaults(FALSE);
        Weekend<ConsigneePo> weekend = of(ConsigneePo.class);
        weekend.weekendCriteria().andEqualTo(ConsigneePo::getUserId, userId)
                .andEqualTo(ConsigneePo::getDefaults, TRUE);
        getMapper().updateByExampleSelective(consigneePo, weekend);
    }

    /**
     * 查询收件人信息，优先返回默认收件人（最多只能有一个）
     */
    public ConsigneePo getConsignee(long userId) {
        ConsigneePo consigneePo = new ConsigneePo();
        consigneePo.setUserId(userId);
        consigneePo.setDefaults(TRUE);
        consigneePo = findOne(consigneePo);
        if (null != consigneePo) {
            return consigneePo;
        }
        return ((ConsigneeMapper) getMapper()).selectOneConsignee(userId);
    }
}
