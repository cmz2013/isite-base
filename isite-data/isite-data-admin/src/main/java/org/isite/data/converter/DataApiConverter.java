package org.isite.data.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.data.po.DataApiPo;
import org.isite.data.support.dto.DataApiDto;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DataApiConverter {

    private DataApiConverter() {
    }

    public static DataApiPo toDataApiPo(DataApiDto dataApiDto) {
        DataApiPo dataApiPo = DataConverter.convert(dataApiDto, DataApiPo::new);
        dataApiPo.setStatus(ActiveStatus.ENABLED);
        return dataApiPo;
    }
}
