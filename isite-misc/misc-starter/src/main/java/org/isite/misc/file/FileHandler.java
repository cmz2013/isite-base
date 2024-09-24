package org.isite.misc.file;

import lombok.extern.slf4j.Slf4j;
import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.data.dto.FileRecordPutDto;
import org.isite.misc.data.enums.FileStatus;
import org.isite.misc.data.vo.FileRecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

import static org.isite.commons.cloud.converter.Converter.convert;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.misc.client.FileRecordAccessor.addFileRecord;
import static org.isite.misc.client.FileRecordAccessor.updateFileRecord;
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
    public FileRecord uploadFile(String fileName, InputStream stream, String target) throws Exception {
        return uploadFile(fileName, stream, target,null);
    }

    /**
     * @Description 上传和解析文件
     * @param fileName 文件名
     * @param input 文件输入流
     * @param target HTTP接口，或FTP文件存放路径，例如分日期存放：/2018/01/01
     * @return 文件上传记录
     */
    public FileRecord uploadFile(
            String fileName, InputStream input, String target, Parser<?> parser) throws Exception {
        storeFile(fileName, input, target);
        FileRecord fileRecord = createFileRecord(fileName, target, UPLOAD_SUCCESS);
        if (null != parser) {
            if (parser.isAsync()) {
                executor.execute(() -> parseFile(fileRecord, parser));
            } else {
                parseFile(fileRecord, parser);
            }
        }
        return fileRecord;
    }

    /**
     * @Description 解析文件
     * @param fileRecord 文件记录
     */
    private void parseFile(FileRecord fileRecord, Parser<?> parser) {
        updateFileStatus(fileRecord.getId(), PARSE_PROCESS, BLANK_STRING);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            downloadFile(fileRecord, output);
            try(InputStream input = new ByteArrayInputStream(output.toByteArray());) {
                fileRecord = parser.execute(fileRecord, input);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fileRecord.setStatus(PARSE_FAILURE);
            fileRecord.setRemark(e.getMessage());
        }
        updateFileStatus(fileRecord.getId(), fileRecord.getStatus(), fileRecord.getRemark());
    }

    private void updateFileStatus(Integer id, FileStatus status, String remark) {
        try {
            updateFileRecord(new FileRecordPutDto(id, status, remark));
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
    public FileRecord exportFile(String fileName, StreamProvider provider, String target) {
        FileRecord fileRecord = createFileRecord(fileName, target, EXPORT_WAITING);
        executor.execute(() -> exportFile(fileRecord, provider));
        return fileRecord;
    }

    /**
     * 异步导出生成文件
     * @param fileRecord 文件记录
     * @param provider 文件流提供者
     */
    private void exportFile(FileRecord fileRecord, StreamProvider provider) {
        try {
            updateFileStatus(fileRecord.getId(), EXPORT_PROCESS, BLANK_STRING);
            storeFile(fileRecord.getFileName(), provider.stream(), fileRecord.getTarget());
            updateFileStatus(fileRecord.getId(), EXPORT_SUCCESS, BLANK_STRING);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            updateFileStatus(fileRecord.getId(), EXPORT_FAILURE, e.getMessage());
        }
    }

    /**
     * @Description 创建文件记录
     * @param fileName 文件名
     * @param target HTTP接口，或FTP文件存放路径
     * @param status 状态
     * @return 文件记录
     */
    private FileRecord createFileRecord(String fileName, String target, FileStatus status) {
        FileRecordPostDto fileRecordPostDto = new FileRecordPostDto();
        fileRecordPostDto.setStatus(status);
        fileRecordPostDto.setFileName(fileName);
        fileRecordPostDto.setTarget(target);
        FileRecord fileRecord = convert(fileRecordPostDto, FileRecord::new);
        fileRecord.setId(addFileRecord(fileRecordPostDto));
        return fileRecord;
    }

    /**
     * @Description 下载文件
     * @param fileRecord 文件记录
     * @param output 流使用完不关闭，可以继续写入
     */
    protected abstract void downloadFile(FileRecord fileRecord, OutputStream output) throws Exception;

}
