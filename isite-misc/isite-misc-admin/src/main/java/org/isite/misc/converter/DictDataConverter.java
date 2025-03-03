package org.isite.misc.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.misc.data.dto.DictDataDto;
import org.isite.misc.po.DictDataPo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DictDataConverter {

    private DictDataConverter() {
    }

    public static DictDataPo toDictDataPo(DictDataDto dictDataDto) {
        DictDataPo dictDataPo = DataConverter.convert(dictDataDto, DictDataPo::new);
        if (null == dictDataPo.getRemark()) {
            dictDataPo.setRemark(Constants.BLANK_STR);
        }
        if (null == dictDataPo.getSort()) {
            dictDataPo.setSort(Constants.ZERO);
        }
        return dictDataPo;
    }
}
