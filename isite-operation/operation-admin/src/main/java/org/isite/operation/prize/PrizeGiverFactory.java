package org.isite.operation.prize;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.operation.support.enums.PrizeType;
import org.springframework.stereotype.Component;

/**
 * @Description 奖品发放接口工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PrizeGiverFactory extends AbstractFactory<PrizeGiver, PrizeType, Integer> {
}
