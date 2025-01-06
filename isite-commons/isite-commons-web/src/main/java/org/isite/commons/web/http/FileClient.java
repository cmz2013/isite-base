package org.isite.commons.web.http;

import lombok.Setter;
import org.isite.commons.cloud.data.enums.HttpMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.isite.commons.cloud.data.constants.HttpHeaders.CONNECTION;
import static org.isite.commons.cloud.data.constants.HttpHeaders.CONTENT_DISPOSITION;
import static org.isite.commons.cloud.data.constants.HttpHeaders.CONTENT_TRANSFER_ENCODING;
import static org.isite.commons.cloud.data.constants.HttpHeaders.CONTENT_TYPE;
import static org.isite.commons.cloud.data.enums.HttpMethod.GET;
import static org.isite.commons.cloud.data.enums.HttpMethod.POST;
import static org.isite.commons.lang.Constants.CHARSET;
import static org.isite.commons.lang.utils.IoUtils.copy;
import static org.isite.commons.web.http.TrustHttps.isHttps;
import static org.isite.commons.web.http.TrustHttps.trustAllHosts;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Setter
public class FileClient {
    /**
     * 换行符
     */
    private static final String LINE_END = "\r\n";
    /**
     * boundary前缀
     */
    private static final String TWO_HYPHENS = "--";
    /**
     * boundary是分隔符。因为上传文件请求内容不再以x = y方式发送了
     */
    private static final String BOUNDARY = randomUUID().toString();
    /**
     * 文件上传form data字段名，默认file
     */
    private String fieldFile = "file";

    private Map<String, String> headers;

    private HttpURLConnection connection(HttpMethod method, String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
        if (isHttps(url)) {
            trustAllHosts((HttpsURLConnection) connection);
        }
        connection.setRequestMethod(method.name());
        if (isNotEmpty(headers)) {
            headers.forEach(connection::setRequestProperty);
        }
        //设置字符编码
        connection.setRequestProperty(CHARSET, UTF_8.name());
        // 不使用缓存
        connection.setUseCaches(FALSE);
        return connection;
    }

    /**
     * 上传文件
     */
    public void upload(String fileName, InputStream input, String url) throws Exception {
        HttpURLConnection connection = connection(POST, url);
        // 设置请求内容类型
        connection.setRequestProperty(CONTENT_TYPE, "multipart/form-data;BOUNDARY=" + BOUNDARY);
        // 是否向connection输出,参数放在http正文内时,要设为true。
        connection.setDoOutput(TRUE);
        connection.setRequestProperty(CONNECTION, "Keep-Alive");
        try (input; DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
            writeFile(fileName, input, output);
            /*
             * 先前写入的数据存在于内存缓冲区中,将流信息刷入缓冲输出流,
             * 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端
             */
            output.flush();
        } finally {
            connection.disconnect();
        }
    }

    private void writeFile(String filename, InputStream input, DataOutputStream output) throws IOException {
        output.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
        /*
         * JAVA中的char是16位的，一个char存储一个中文字符。如果用writeBytes方法转换会变为8位byte，
         * 直接导致高8位丢失，从而导致中文乱码。所以，这里使用write方法
         */
        output.write((CONTENT_DISPOSITION + ": form-data; name=\"" +
                fieldFile + "\"; filename=\"" + filename + "\"").getBytes());
        output.writeBytes(LINE_END);
        output.writeBytes(CONTENT_TYPE + ": application/octet-stream" + LINE_END);
        output.writeBytes(CONTENT_TRANSFER_ENCODING + ": binary" + LINE_END);
        //这里是一个空行（不可少）
        output.writeBytes(LINE_END);

        // 写入文件
        copy(input, output);
        output.writeBytes(LINE_END);
        //添加结束标识
        output.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);
    }

    /**
     * 下载文件。output不关闭，可以继续写入数据
     */
    public void download(String url, OutputStream output) throws Exception {
        HttpURLConnection connection = null;
        try {
            connection = connection(GET, url);
            connection.connect();
            copy(connection.getInputStream(), output);
        } finally {
            if (null != connection) {
                connection.disconnect();
            }
        }
    }

}
