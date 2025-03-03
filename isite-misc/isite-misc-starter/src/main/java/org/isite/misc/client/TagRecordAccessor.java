package org.isite.misc.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.misc.data.constants.MiscConstants;
import org.isite.misc.data.dto.TagRecordDto;
/**
 * @Description TagRecordClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class TagRecordAccessor {

    private TagRecordAccessor() {
    }

    public static Integer addTagRecord(TagRecordDto recordDto) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        TagRecordClient tagRecordClient = feignClientFactory.getFeignClient(TagRecordClient.class, MiscConstants.SERVICE_ID);
        return ResultUtils.getData(tagRecordClient.addTagRecord(recordDto));
    }

}
