package org.isite.operation.service;

import org.isite.mybatis.service.PoService;
import org.isite.operation.mapper.PrizeDeliverMapper;
import org.isite.operation.po.PrizeDeliverPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;

import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeDeliverService extends PoService<PrizeDeliverPo, Integer> {

    @Autowired
    public PrizeDeliverService(PrizeDeliverMapper mapper) {
        super(mapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updatePrizeDeliver(Long prizeRecordId, PrizeDeliverPo prizeDeliverPo) {
        Weekend<PrizeDeliverPo> weekend = of(PrizeDeliverPo.class);
        weekend.weekendCriteria().andEqualTo(PrizeDeliverPo::getPrizeRecordId, prizeRecordId);
        return getMapper().updateByExampleSelective(prizeDeliverPo, weekend);
    }
}
