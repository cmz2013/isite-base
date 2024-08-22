package org.isite.operation.service;

import org.isite.mybatis.service.PoService;
import org.isite.operation.mapper.PrizeCodeMapper;
import org.isite.operation.po.PrizeCodePo;
import org.isite.operation.po.PrizePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.data.Constants.ZERO;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeCodeService extends PoService<PrizeCodePo, Integer> {

    private PrizeService prizeService;

    @Autowired
    public PrizeCodeService(PrizeCodeMapper prizeCodeMapper) {
        super(prizeCodeMapper);
    }

    /**
     * 发放兑奖码
     */
    @Transactional(rollbackFor = Exception.class)
    public String grantPrizeCode(Long userId, Integer prizeId) {
        //根据奖品ID查询未使用的兑奖码
        PrizeCodePo codePo = ((PrizeCodeMapper) getMapper()).selectOneUnused(prizeId);
        notNull(codePo, getMessage("Prize.notEnough", " have all been taken away"));
        PrizeCodePo prizeCodePo = new PrizeCodePo();
        prizeCodePo.setId(codePo.getId());
        prizeCodePo.setUserId(userId);
        this.updateSelectiveById(prizeCodePo);
        return codePo.getCode();
    }

    /**
     * 删除兑奖码时更新奖品库存，已被领取的兑奖码无法删除
     */
    @Transactional(rollbackFor = Exception.class)
    public int deletePrizeCodes(PrizePo prizePo, List<Integer> ids) {
        int rows = getMapper().deleteByExample(of(PrizeCodePo.class).weekendCriteria()
                .andEqualTo(PrizeCodePo::getPrizeId, prizePo.getId())
                .andIn(PrizeCodePo::getId, ids)
                .andEqualTo(PrizeCodePo::getUserId, ZERO));
        isTrue(rows == ids.size(), getMessage("received.notDelete",
                "It cannot be deleted if it has already been received"));
        prizeService.updateTotalInventory(prizePo.getId(), prizePo.getTotalInventory() - rows);
        return rows;
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    /**
     * 导入兑奖码，更新奖品库存
     */
    @Transactional(rollbackFor = Exception.class)
    public int addPrizeCodes(PrizePo prizePo, Set<String> codes) {
        // rows的最大值取决于codes.size() int，所以将rows转换为int不会丢失精度
        int rows = insert(codes.stream().map(code ->
                new PrizeCodePo(prizePo.getActivityId(), prizePo.getId(), code)).collect(toList()));
        prizeService.updateTotalInventory(prizePo.getId(), prizePo.getTotalInventory() + rows);
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deletePrize(Integer prizeId) {
        delete(PrizeCodePo::getPrizeId, prizeId);
        return prizeService.delete(prizeId);
    }
}
