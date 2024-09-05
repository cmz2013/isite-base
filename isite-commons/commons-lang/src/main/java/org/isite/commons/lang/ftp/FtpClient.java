package org.isite.commons.lang.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;
import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.SLASH;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FtpClient {

    private final FtpProperties properties;

    public FtpClient(FtpProperties properties) {
        this.properties = properties;
    }

    private FTPClient initFtpClient() throws IOException {
        FTPClient client = new FTPClient();
        client.connect(properties.getHost(), properties.getPort());
        client.login(properties.getUserName(), properties.getPassword());
        client.enterLocalPassiveMode();
        if (!isPositiveCompletion(client.getReplyCode())) {
            client.disconnect();
            throw new IOException("login failure");
        }
        client.setControlEncoding(UTF_8.name());
        client.setFileType(BINARY_FILE_TYPE);
        return client;
    }

    /**
     * 创建并切换目录(一级)
     */
    private boolean changeDirectory(String pathname, FTPClient client) throws IOException {
        if (isBlank(pathname) || client.changeWorkingDirectory(pathname)) {
            return true;
        }
        return client.makeDirectory(pathname) && client.changeWorkingDirectory(pathname);
    }

    /**
     * 创建并切换多级目录
     */
    private boolean changeDirectory(String[] pathnames, FTPClient client) throws IOException {
        if (isEmpty(pathnames)) {
            return true;
        }
        //如果目录不存在创建目录,ftp是不可以嵌套创建目录的
        for (String pathname : pathnames) {
            if (!changeDirectory(pathname, client)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 上传文件
     */
    public void upload(String filename, InputStream stream, String pathname) throws IOException {
        FTPClient client = initFtpClient();
        try {
            isTrue(changeDirectory(pathname.split(SLASH), client), "change directory failure: " + pathname);
            //FTP协议里面，规定文件名编码为iso-8859-1
            isTrue(client.storeFile(new String(filename.getBytes(), ISO_8859_1), stream),
                    "failed to upload file: " + filename);
        } finally {
            //释放FTPClient连接
            client.disconnect();
        }
    }

    /**
     * 下载文件。output不关闭可以继续写入
     */
    public void download(String pathname, String filename, OutputStream output) throws IOException {
        FTPClient client = initFtpClient();
        try {
            if (client.changeWorkingDirectory(pathname)) {
                client.retrieveFile(new String(filename.getBytes(), ISO_8859_1), output);
            }
        } finally {
            //释放FTPClient连接
            client.disconnect();
        }
    }

    /**
     * 删除文件
     */
    public boolean delete(String pathname, String filename) throws Exception {
        FTPClient client = initFtpClient();
        try {
            return client.changeWorkingDirectory(pathname) &&
                    client.deleteFile(new String(filename.getBytes(), ISO_8859_1));
        } finally {
            client.disconnect();
        }
    }
}
