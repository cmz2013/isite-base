package org.isite.operation.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.po.PrizeDeliverPo;
import org.isite.operation.support.dto.PrizeDeliverDto;
import org.isite.user.data.dto.ConsigneeDto;

import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeDeliverConverter {

    private PrizeDeliverConverter() {
    }

    public static PrizeDeliverPo toPrizeDeliverPo(long prizeRecordId, ConsigneeDto consigneeDto) {
        PrizeDeliverPo deliverPo = DataConverter.convert(consigneeDto, PrizeDeliverPo::new);
        deliverPo.setPrizeRecordId(prizeRecordId);
        deliverPo.setFrId(TypeUtils.cast(Constants.ZERO));
        deliverPo.setOrderNum(Constants.BLANK_STR);
        return deliverPo;
    }

    public static PrizeDeliverPo toPrizeDeliverSelectivePo(long frId, PrizeDeliverDto prizeDeliverDto) {
        PrizeDeliverPo deliverPo = DataConverter.convert(prizeDeliverDto, PrizeDeliverPo::new);
        deliverPo.setFrId(frId);
        deliverPo.setDeliverTime(LocalDateTime.now());
        return deliverPo;
    }
}
