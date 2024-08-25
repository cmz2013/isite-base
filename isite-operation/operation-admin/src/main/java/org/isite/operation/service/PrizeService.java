package org.isite.operation.service;

import org.isite.commons.web.sync.ConcurrentError;
import org.isite.mybatis.service.PoService;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.mapper.PrizeMapper;
import org.isite.operation.po.PrizePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.ZERO;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeService extends PoService<PrizePo, Integer> {

    @Autowired
    public PrizeService(PrizeMapper prizeMapper) {
        super(prizeMapper);
    }

    /**
     * 获取可用奖品
     */
    public List<Prize> filterPrizes(List<Prize> prizes) {
        if (isEmpty(prizes)) {
            return null;
        }
        return prizes.stream().filter(prizeVo -> prizeVo.getTotalInventory() < ZERO
                || prizeVo.getTotalInventory() - prizeVo.getConsumeInventory() > ZERO).collect(toList());
    }

    /**
     * 扣减已锁定库存
     */
    @Transactional(rollbackFor = Exception.class)
    public int decrLockInventory(int prizeId, int lockInventory) {
        PrizePo prizePo = new PrizePo();
        prizePo.setLockInventory(lockInventory - ONE);
        Weekend<PrizePo> weekend = of(PrizePo.class);
        weekend.weekendCriteria().andEqualTo(PrizePo::getId, prizeId)
                // 使用乐观锁控制并发场景
                .andEqualTo(PrizePo::getLockInventory, lockInventory);
        isTrue(getMapper().updateByExampleSelective(prizePo, weekend) > ZERO, new ConcurrentError());
        return prizePo.getLockInventory();
    }

    /**
     * 回滚锁定库存和已消耗库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void rollbackLockInventory(Integer prizeId) {
        PrizePo prizePo = get(prizeId);
        int lockInventory = prizePo.getLockInventory() - ONE;
        int consumeInventory = prizePo.getConsumeInventory() - ONE;
        PrizePo updatePo = new PrizePo();
        updatePo.setId(prizePo.getId());
        updatePo.setLockInventory(lockInventory);
        updatePo.setConsumeInventory(consumeInventory);
        updateSelectiveById(updatePo);
    }

    /**
     * 增加已锁定库存和已消耗库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void incrLockInventory(PrizePo prizePo) {
        int lockInventory = prizePo.getLockInventory() + ONE;
        int consumeInventory = prizePo.getConsumeInventory() + ONE;
        if (prizePo.getTotalInventory() > ZERO) {
            isTrue(prizePo.getTotalInventory() >= consumeInventory,
                    getMessage("prize.notInventory", "the prize is gone"));
        }

        PrizePo updatePo = new PrizePo();
        updatePo.setId(prizePo.getId());
        updatePo.setLockInventory(lockInventory);
        updatePo.setConsumeInventory(consumeInventory);
        updateSelectiveById(updatePo);
    }

    /**
     * 增加已消耗库存
     */
    @Transactional(rollbackFor = Exception.class)
    public int incrConsumeInventory(int prizeId, int consumeInventory) {
        PrizePo prizePo = new PrizePo();
        prizePo.setConsumeInventory(consumeInventory + ONE);
        Weekend<PrizePo> weekend = of(PrizePo.class);
        weekend.weekendCriteria().andEqualTo(PrizePo::getId, prizeId)
                // 使用乐观锁控制并发场景
                .andEqualTo(PrizePo::getConsumeInventory, consumeInventory);
        isTrue(getMapper().updateByExampleSelective(prizePo, weekend) > ZERO, new ConcurrentError());
        return prizePo.getConsumeInventory();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTotalInventory(Integer id, int totalInventory) {
        PrizePo prizePo = new PrizePo();
        prizePo.setId(id);
        prizePo.setTotalInventory(totalInventory);
        updateSelectiveById(prizePo);
    }
}
