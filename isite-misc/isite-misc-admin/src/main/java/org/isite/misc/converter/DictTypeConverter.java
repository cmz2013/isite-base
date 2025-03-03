package org.isite.misc.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.misc.data.dto.DictTypeDto;
import org.isite.misc.po.DictTypePo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DictTypeConverter {

    private DictTypeConverter() {
    }

    public static DictTypePo toDictTypePo(DictTypeDto dictTypeDto) {
        DictTypePo dictTypePo = DataConverter.convert(dictTypeDto, DictTypePo::new);
        if (null == dictTypePo.getRemark()) {
            dictTypePo.setRemark(Constants.BLANK_STR);
        }
        return dictTypePo;
    }
}
