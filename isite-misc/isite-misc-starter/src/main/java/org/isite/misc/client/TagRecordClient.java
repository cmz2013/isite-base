package org.isite.misc.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.misc.data.constants.MiscUrls;
import org.isite.misc.data.dto.TagRecordDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "tagRecordClient", value = SERVICE_ID)
public interface TagRecordClient {

    @PostMapping(MiscUrls.POST_TAG_RECORD)
    Result<Integer> addTagRecord(@RequestBody TagRecordDto recordDto);

}
