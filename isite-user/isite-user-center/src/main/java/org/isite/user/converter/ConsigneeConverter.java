package org.isite.user.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.user.data.dto.ConsigneeDto;
import org.isite.user.po.ConsigneePo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ConsigneeConverter {

    private ConsigneeConverter() {
    }

    public static ConsigneePo toConsigneePo(ConsigneeDto consigneeDto) {
        ConsigneePo consigneePo = DataConverter.convert(consigneeDto, ConsigneePo::new);
        consigneePo.setDefaults(Boolean.FALSE);
        consigneePo.setUserId(TransmittableHeaders.getUserId());
        return consigneePo;
    }
}
