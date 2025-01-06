package org.isite.operation.converter;

import org.isite.operation.po.PrizeDeliverPo;
import org.isite.operation.support.dto.PrizeDeliverDto;
import org.isite.user.data.dto.ConsigneeDto;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeDeliverConverter {

    private PrizeDeliverConverter() {
    }

    public static PrizeDeliverPo toPrizeDeliverPo(long prizeRecordId, ConsigneeDto consigneeDto) {
        PrizeDeliverPo deliverPo = convert(consigneeDto, PrizeDeliverPo::new);
        deliverPo.setPrizeRecordId(prizeRecordId);
        deliverPo.setFrId((long) ZERO);
        deliverPo.setOrderNum(BLANK_STR);
        return deliverPo;
    }

    public static PrizeDeliverPo toPrizeDeliverSelectivePo(long frId, PrizeDeliverDto prizeDeliverDto) {
        PrizeDeliverPo deliverPo = convert(prizeDeliverDto, PrizeDeliverPo::new);
        deliverPo.setFrId(frId);
        deliverPo.setDeliverTime(new Date(currentTimeMillis()));
        return deliverPo;
    }
}
