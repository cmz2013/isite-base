package org.isite.misc.file;

import org.isite.commons.web.http.FileClient;
import org.isite.misc.data.vo.FileRecord;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

/**
 * @Description Http File Client
 * @Author <font color='blue'>zhangcm</font>
 */
public class HttpFileHandler extends FileHandler {

    private final FileClient fileClient;

    public HttpFileHandler(Executor executor, FileClient fileClient) {
        super(executor);
        this.fileClient = fileClient;
    }

    @Override
    protected void storeFile(String fileName, InputStream input, String url) throws Exception {
        fileClient.upload(fileName, input, url);
    }

    @Override
    protected void downloadFile(FileRecord fileRecord, OutputStream output) throws Exception {
        fileClient.download(fileRecord.getTarget(), output);
    }

}
