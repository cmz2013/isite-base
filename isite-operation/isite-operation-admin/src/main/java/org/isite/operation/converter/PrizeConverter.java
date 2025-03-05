package org.isite.operation.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.operation.po.PrizePo;
import org.isite.operation.support.dto.PrizePostDto;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeConverter {

    private PrizeConverter() {
    }

    public static PrizePo toPrizePo(PrizePostDto prizePostDto) {
        PrizePo prizePo = DataConverter.convert(prizePostDto, PrizePo::new);
        if (null == prizePo.getProbability()) {
            prizePo.setProbability(Constants.ZERO);
        }
        if (null == prizePo.getSort()) {
            prizePo.setSort(Constants.ZERO);
        }
        if (null == prizePo.getThirdPrizeValue()) {
            prizePo.setThirdPrizeValue(Constants.BLANK_STR);
        }
        if (null == prizePo.getPrizeImage()) {
            prizePo.setPrizeImage(Constants.BLANK_STR);
        }
        if (null == prizePo.getRemark()) {
            prizePo.setRemark(Constants.BLANK_STR);
        }
        return prizePo;
    }
}
