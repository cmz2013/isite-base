package org.isite.misc.converter;

import org.isite.misc.data.dto.DictTypeDto;
import org.isite.misc.po.DictTypePo;

import static org.isite.commons.cloud.converter.Converter.convert;
import static org.isite.commons.lang.Constants.BLANK_STRING;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DictTypeConverter {

    private DictTypeConverter() {
    }

    public static DictTypePo toDictTypePo(DictTypeDto dictTypeDto) {
        DictTypePo dictTypePo = convert(dictTypeDto, DictTypePo::new);
        if (null == dictTypePo.getRemark()) {
            dictTypePo.setRemark(BLANK_STRING);
        }
        return dictTypePo;
    }
}
