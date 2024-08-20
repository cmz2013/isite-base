package org.isite.misc.file;

import lombok.Setter;
import org.isite.commons.lang.ftp.FtpClient;
import org.isite.misc.data.dto.FileRecordDto;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

/**
 * @Description FTP客户端
 * @Author <font color='blue'>zhangcm</font>
 */
@Setter
public class FtpFileHandler extends FileHandler {

    private final FtpClient ftpClient;

    public FtpFileHandler(Executor executor, FtpClient ftpClient) {
        super(executor);
        this.ftpClient = ftpClient;
    }

    @Override
    protected void storeFile(String fileName, InputStream stream, String target) throws Exception {
        ftpClient.upload(fileName, stream, target);
    }

    /**
     * 从FTP服务器下载文件
     * @param fileRecordDto 文件信息
     * @param output 文件下载后输出流
     */
    @Override
    protected void downloadFile(FileRecordDto fileRecordDto, OutputStream output) throws Exception {
        ftpClient.download(fileRecordDto.getTarget(), fileRecordDto.getFileName(), output);
    }
}
