package org.isite.operation.converter;

import org.isite.operation.data.dto.PrizePostDto;
import org.isite.operation.po.PrizePo;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.data.Constants.BLANK_STRING;
import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeConverter {

    private PrizeConverter() {
    }

    public static PrizePo toPrizePo(PrizePostDto prizePostDto) {
        PrizePo prizePo = convert(prizePostDto, PrizePo::new);
        if (null == prizePo.getProbability()) {
            prizePo.setProbability(ZERO);
        }
        if (null == prizePo.getSort()) {
            prizePo.setSort(ZERO);
        }
        if (null == prizePo.getThirdPrizeValue()) {
            prizePo.setThirdPrizeValue(BLANK_STRING);
        }
        if (null == prizePo.getPrizeImage()) {
            prizePo.setPrizeImage(BLANK_STRING);
        }
        if (null == prizePo.getRemark()) {
            prizePo.setRemark(BLANK_STRING);
        }
        return prizePo;
    }
}
