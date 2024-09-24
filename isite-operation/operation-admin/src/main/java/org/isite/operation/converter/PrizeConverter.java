package org.isite.operation.converter;

import org.isite.operation.po.PrizePo;
import org.isite.operation.support.dto.PrizePostDto;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.ZERO;

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
            prizePo.setThirdPrizeValue(BLANK_STR);
        }
        if (null == prizePo.getPrizeImage()) {
            prizePo.setPrizeImage(BLANK_STR);
        }
        if (null == prizePo.getRemark()) {
            prizePo.setRemark(BLANK_STR);
        }
        return prizePo;
    }
}
