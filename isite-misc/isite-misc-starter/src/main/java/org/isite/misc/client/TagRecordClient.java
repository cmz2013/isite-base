package org.isite.misc.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.misc.data.dto.TagRecordDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.isite.misc.data.constants.UrlConstants.POST_TAG_RECORD;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "tagRecordClient", value = SERVICE_ID)
public interface TagRecordClient {

    @PostMapping(POST_TAG_RECORD)
    Result<Integer> addTagRecord(@RequestBody TagRecordDto recordDto);

}
