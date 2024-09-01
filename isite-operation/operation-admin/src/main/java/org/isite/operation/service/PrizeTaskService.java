package org.isite.operation.service;

import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeReward;
import org.isite.operation.support.vo.PrizeTaskProperty;
import org.isite.operation.support.vo.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.schedule.ProbabilityScheduler.choose;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeTaskService {

    private PrizeService prizeService;

    public Reward getReward(List<Prize> prizes, PrizeTaskProperty prizeTaskProperty) {
        if (null == prizeTaskProperty || isEmpty(prizeTaskProperty.getRewards())) {
            return null;
        }
        List<PrizeReward> rewards = prizeTaskProperty.getRewards();
        if (rewards.size() == ONE) {
            return rewards.get(ZERO);
        }
        prizes = prizeService.filterPrizes(prizes);
        if (isEmpty(prizes)) {
            return null;
        }

        List<Integer> prizeIds = rewards.stream().map(PrizeReward::getPrizeId).collect(toList());
        prizes = prizes.stream().filter(prize -> prizeIds.contains(prize.getId())).collect(toList());
        Prize prize = choose(prizes, Prize::getProbability);
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
