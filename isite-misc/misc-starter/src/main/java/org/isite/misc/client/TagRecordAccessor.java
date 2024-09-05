package org.isite.misc.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.misc.data.dto.TagRecordDto;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.ResultUtils.getData;
import static org.isite.misc.data.constants.MiscConstants.SERVICE_ID;

/**
 * @Description TagRecordClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class TagRecordAccessor {

    private TagRecordAccessor() {
    }

    public static Integer addTagRecord(TagRecordDto recordDto) {
        TagRecordClient tagRecordClient = getBean(FeignClientFactory.class).getFeignClient(TagRecordClient.class, SERVICE_ID);
        return getData(tagRecordClient.addTagRecord(recordDto));
    }

}
