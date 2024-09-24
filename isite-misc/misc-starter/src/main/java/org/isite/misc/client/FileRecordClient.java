package org.isite.misc.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.data.dto.FileRecordPutDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.isite.misc.data.constants.UrlConstants.MY_POST_FILE_RECORD;
import static org.isite.misc.data.constants.UrlConstants.PUT_FILE_RECORD;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "fileRecordClient", value = SERVICE_ID)
public interface FileRecordClient {

    /**
     * 根据ID更新文件记录，返回更新条数
     */
    @PutMapping(PUT_FILE_RECORD)
    Result<Integer> updateFileRecord(@RequestBody FileRecordPutDto fileRecordPutDto);

    /**
     * 添加文件记录，返回ID
     */
    @PostMapping(MY_POST_FILE_RECORD)
    Result<Integer> addFileRecord(@RequestBody FileRecordPostDto fileRecordPostDto);
}
