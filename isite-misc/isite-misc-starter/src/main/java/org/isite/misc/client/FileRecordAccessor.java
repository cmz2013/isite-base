package org.isite.misc.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.misc.data.constants.MiscConstants;
import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.data.dto.FileRecordPutDto;
/**
 * @Description FileRecordClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class FileRecordAccessor {

    private FileRecordAccessor() {
    }

    /**
     * 根据ID更新文件记录，返回更新条数
     */
    public static Integer updateFileRecord(FileRecordPutDto fileRecordPutDto) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        FileRecordClient fileRecordClient = feignClientFactory.getFeignClient(FileRecordClient.class, MiscConstants.SERVICE_ID);
        return ResultUtils.getData(fileRecordClient.updateFileRecord(fileRecordPutDto));
    }

    /**
     * 添加文件记录，返回ID
     */
    public static Integer addFileRecord(FileRecordPostDto fileRecordPostDto) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        FileRecordClient fileRecordClient = feignClientFactory.getFeignClient(FileRecordClient.class, MiscConstants.SERVICE_ID);
        return ResultUtils.getData(fileRecordClient.addFileRecord(fileRecordPostDto));
    }
}
