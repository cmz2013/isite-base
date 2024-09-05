package org.isite.misc.converter;

import org.isite.misc.data.dto.DictDataDto;
import org.isite.misc.po.DictDataPo;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DictDataConverter {

    private DictDataConverter() {
    }

    public static DictDataPo toDictDataPo(DictDataDto dictDataDto) {
        DictDataPo dictDataPo = convert(dictDataDto, DictDataPo::new);
        if (null == dictDataPo.getRemark()) {
            dictDataPo.setRemark(BLANK_STRING);
        }
        if (null == dictDataPo.getSort()) {
            dictDataPo.setSort(ZERO);
        }
        return dictDataPo;
    }
}
