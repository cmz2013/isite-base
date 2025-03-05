package org.isite.operation.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.schedule.ProbabilityScheduler;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeReward;
import org.isite.operation.support.vo.PrizeTaskProperty;
import org.isite.operation.support.vo.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeTaskService {
    private PrizeService prizeService;

    public Reward getReward(List<Prize> prizes, PrizeTaskProperty prizeTaskProperty) {
        if (null == prizeTaskProperty || CollectionUtils.isEmpty(prizeTaskProperty.getRewards())) {
            return null;
        }
        List<PrizeReward> rewards = prizeTaskProperty.getRewards();
        if (rewards.size() == Constants.ONE) {
            return rewards.get(Constants.ZERO);
        }
        prizes = prizeService.filterPrizes(prizes);
        if (CollectionUtils.isEmpty(prizes)) {
            return null;
        }
        List<Integer> prizeIds = rewards.stream().map(PrizeReward::getPrizeId).collect(Collectors.toList());
        prizes = prizes.stream().filter(prize -> prizeIds.contains(prize.getId())).collect(Collectors.toList());
        Prize prize = ProbabilityScheduler.choose(prizes, Prize::getProbability);
        if (null == prize) {
            return null;
        }
        for (PrizeReward reward : rewards) {
            if (prize.getId().equals(reward.getPrizeId())) {
                return reward;
            }
        }
        return null;
    }


    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }
}
