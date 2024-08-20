package org.isite.misc.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.misc.data.dto.FileRecordDto;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.lang.utils.ResultUtils.getData;
import static org.isite.misc.data.constants.MiscConstants.SERVICE_ID;

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
    public static Integer updateFileRecord(FileRecordDto fileRecordDto) {
        FileRecordClient fileRecordClient = getBean(FeignClientFactory.class).getFeignClient(FileRecordClient.class, SERVICE_ID);
        return getData(fileRecordClient.updateFileRecord(fileRecordDto));
    }

    /**
     * 添加文件记录，返回ID
     */
    public static Integer addFileRecord(FileRecordDto fileRecordDto) {
        FileRecordClient fileRecordClient = getBean(FeignClientFactory.class).getFeignClient(FileRecordClient.class, SERVICE_ID);
        return getData(fileRecordClient.addFileRecord(fileRecordDto));
    }
}
