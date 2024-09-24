package org.isite.operation.converter;

import org.isite.operation.po.PrizeCodePo;
import org.isite.operation.support.dto.PrizeCodeDto;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeCodeConverter {

    private PrizeCodeConverter() {
    }

    public static PrizeCodePo toPrizeCodeSelectivePo(Integer prizeId, PrizeCodeDto prizeCodeDto) {
        PrizeCodePo prizeCodePo = new PrizeCodePo();
        prizeCodePo.setUserId(prizeCodeDto.getUserId());
        prizeCodePo.setPrizeId(prizeId);
        return prizeCodePo;
    }
}
