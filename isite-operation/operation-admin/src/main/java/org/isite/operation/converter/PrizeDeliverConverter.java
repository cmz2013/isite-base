package org.isite.operation.converter;

import org.isite.operation.po.PrizeDeliverPo;
import org.isite.operation.data.dto.PrizeDeliverDto;
import org.isite.user.data.dto.ConsigneeDto;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeDeliverConverter {

    private PrizeDeliverConverter() {
    }

    public static PrizeDeliverPo toPrizeDeliverPo(long prizeRecordId, ConsigneeDto consigneeDto) {
        PrizeDeliverPo deliverPo = new PrizeDeliverPo();
        deliverPo.setPrizeRecordId(prizeRecordId);
        copyProperties(consigneeDto, deliverPo);
        return deliverPo;
    }

    public static PrizeDeliverPo toPrizeDeliverPo(long frId, PrizeDeliverDto deliverDto) {
        PrizeDeliverPo deliverPo = new PrizeDeliverPo();
        deliverPo.setFrId(frId);
        deliverPo.setDeliverTime(new Date(currentTimeMillis()));
        copyProperties(deliverDto, deliverPo);
        return deliverPo;
    }
}
