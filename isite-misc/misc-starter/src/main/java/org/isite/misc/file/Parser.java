package org.isite.misc.file;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.isite.misc.data.vo.FileRecord;

import java.io.InputStream;

import static java.lang.Boolean.TRUE;
import static org.isite.misc.data.enums.FileStatus.PARSE_FAILURE;
import static org.isite.misc.data.enums.FileStatus.PARSE_SUCCESS;

/**
 * @Description 解析文件
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Getter
@Setter
public abstract class Parser<T> {

    /**
     * @Description 是否异步解析
     */
    private boolean async = TRUE;

    /**
     * @Description 解析文件
     * @param fileRecord 文件记录
     * @param input 文件输入流
     */
    public FileRecord execute(FileRecord fileRecord, InputStream input) {
        try {
            fileRecord.setStatus(handle(toData(input)) ? PARSE_SUCCESS : PARSE_FAILURE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fileRecord.setStatus(PARSE_FAILURE);
            fileRecord.setRemark(e.getMessage());
        }
        return fileRecord;
    }

    /**
     * @Description 处理数据模型
     * @return 处理成功或失败
     */
    protected abstract boolean handle(T data);

    /**
     * @Description 将文件流转为数据模型
     */
    protected abstract T toData(InputStream input) throws Exception;

}
