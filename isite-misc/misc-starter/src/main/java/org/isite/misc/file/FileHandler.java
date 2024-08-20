package org.isite.misc.file;

import lombok.extern.slf4j.Slf4j;
import org.isite.misc.client.FileRecordAccessor;
import org.isite.misc.data.dto.FileRecordDto;
import org.isite.misc.data.enums.FileStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

import static org.isite.misc.data.enums.FileStatus.EXPORT_FAILURE;
import static org.isite.misc.data.enums.FileStatus.EXPORT_PROCESS;
import static org.isite.misc.data.enums.FileStatus.EXPORT_SUCCESS;
import static org.isite.misc.data.enums.FileStatus.EXPORT_WAITING;
import static org.isite.misc.data.enums.FileStatus.PARSE_FAILURE;
import static org.isite.misc.data.enums.FileStatus.PARSE_PROCESS;
import static org.isite.misc.data.enums.FileStatus.UPLOAD_SUCCESS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public abstract class FileHandler {
    /**
     * 异步操作：文件解析、异步导出生成文件
     */
    private final Executor executor;

    protected FileHandler(Executor executor) {
        this.executor = executor;
    }

    /**
     * @Description 上传文件
     * @param fileName 文件名
     * @param stream 文件输入流
     * @param target HTTP接口，或FTP文件存放路径，例如分日期存放：/2018/01/01
     * @return 文件上传记录
     */
    public FileRecordDto uploadFile(String fileName, InputStream stream, String target) throws Exception {
        return uploadFile(fileName, stream, target,null);
    }

    /**
     * @Description 上传和解析文件
     * @param fileName 文件名
     * @param input 文件输入流
     * @param target HTTP接口，或FTP文件存放路径，例如分日期存放：/2018/01/01
     * @return 文件上传记录
     */
    public FileRecordDto uploadFile(
            String fileName, InputStream input, String target, Parser<?> parser) throws Exception {
        storeFile(fileName, input, target);
        FileRecordDto fileRecordDto = addFileRecord(fileName, target, UPLOAD_SUCCESS);
        if (null != parser) {
            if (parser.isAsync()) {
                executor.execute(() -> parseFile(fileRecordDto, parser));
            } else {
                parseFile(fileRecordDto, parser);
            }
        }
        return fileRecordDto;
    }

    /**
     * @Description 解析文件
     * @param fileRecordDto 文件记录
     */
    private void parseFile(FileRecordDto fileRecordDto, Parser<?> parser) {
        fileRecordDto.setStatus(PARSE_PROCESS);
        updateFileRecord(fileRecordDto);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            downloadFile(fileRecordDto, output);
            try(InputStream input = new ByteArrayInputStream(output.toByteArray());) {
                fileRecordDto = parser.execute(fileRecordDto, input);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fileRecordDto.setStatus(PARSE_FAILURE);
            fileRecordDto.setRemark(e.getMessage());
        }
        updateFileRecord(fileRecordDto);
    }

    private void updateFileRecord(FileRecordDto fileRecordDto) {
        try {
            FileRecordAccessor.updateFileRecord(fileRecordDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @Description 存储文件
     * @param fileName 文件名
     * @param input 文件流
     * @param target HTTP接口，或FTP文件存放路径
     */
    protected abstract void storeFile(String fileName, InputStream input, String target) throws Exception;

    /**
     * @Description 异步导出生成文件
     * @param fileName 文件名
     * @param provider 构造文件流
     * @param target HTTP接口，或FTP文件存放路径，例如分日期存放：/2018/01/01
     * @return 文件记录
     */
    public FileRecordDto exportFile(String fileName, StreamProvider provider, String target) {
        FileRecordDto fileRecordDto = addFileRecord(fileName, target, EXPORT_WAITING);
        executor.execute(() -> exportFile(fileRecordDto, provider));
        return fileRecordDto;
    }

    /**
     * 异步导出生成文件
     * @param fileRecordDto 文件记录
     * @param provider 文件流提供者
     */
    private void exportFile(FileRecordDto fileRecordDto, StreamProvider provider) {
        try {
            fileRecordDto.setStatus(EXPORT_PROCESS);
            updateFileRecord(fileRecordDto);
            storeFile(fileRecordDto.getFileName(), provider.stream(), fileRecordDto.getTarget());
            fileRecordDto.setStatus(EXPORT_SUCCESS);
            updateFileRecord(fileRecordDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fileRecordDto.setStatus(EXPORT_FAILURE);
            fileRecordDto.setRemark(e.getMessage());
            updateFileRecord(fileRecordDto);
        }
    }

    /**
     * @Description 添加文件记录
     * @param fileName 文件名
     * @param target HTTP接口，或FTP文件存放路径
     * @param status 状态
     * @return 文件记录
     */
    private FileRecordDto addFileRecord(String fileName, String target, FileStatus status) {
        FileRecordDto fileRecordDto = new FileRecordDto();
        fileRecordDto.setStatus(status);
        fileRecordDto.setFileName(fileName);
        fileRecordDto.setTarget(target);
        fileRecordDto.setId(FileRecordAccessor.addFileRecord(fileRecordDto));
        return fileRecordDto;
    }

    /**
     * @Description 下载文件
     * @param fileRecordDto 文件记录
     * @param output 流使用完不关闭，可以继续写入
     */
    protected abstract void downloadFile(FileRecordDto fileRecordDto, OutputStream output) throws Exception;

}
