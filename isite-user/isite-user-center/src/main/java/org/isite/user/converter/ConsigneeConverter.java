package org.isite.user.converter;

import org.isite.user.data.dto.ConsigneeDto;
import org.isite.user.po.ConsigneePo;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ConsigneeConverter {

    private ConsigneeConverter() {
    }

    public static ConsigneePo toConsigneePo(ConsigneeDto consigneeDto) {
        ConsigneePo consigneePo = convert(consigneeDto, ConsigneePo::new);
        consigneePo.setDefaults(FALSE);
        consigneePo.setUserId(getUserId());
        return consigneePo;
    }
}
