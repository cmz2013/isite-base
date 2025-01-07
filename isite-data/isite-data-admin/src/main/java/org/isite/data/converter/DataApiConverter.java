package org.isite.data.converter;

import org.isite.data.po.DataApiPo;
import org.isite.data.support.dto.DataApiDto;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.enums.ActiveStatus.ENABLED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DataApiConverter {

    private DataApiConverter() {
    }

    public static DataApiPo toDataApiPo(DataApiDto dataApiDto) {
        DataApiPo dataApiPo = convert(dataApiDto, DataApiPo::new);
        dataApiPo.setStatus(ENABLED);
        return dataApiPo;
    }
}
