package org.isite.commons.web.http;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.ContentType;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.cloud.data.enums.HttpMethod;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.lang.utils.IoUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
/**
 * @Description HTTP Client
 * @Author <font color='blue'>zhangcm</font>
 */
public class HttpClient {

    private HttpClient() {
    }

    public static String post(String uri, Map<String, Object> params) throws IOException, URISyntaxException {
        return request(HttpMethod.POST, uri, null, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String post(String uri, Map<String, String> headers, Map<String, Object> params) throws IOException, URISyntaxException {
        return request(HttpMethod.POST, uri, headers, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String post(String uri, String params) throws IOException, URISyntaxException {
        return request(HttpMethod.POST, uri, null, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String post(String uri, Map<String, String> headers, String params) throws IOException, URISyntaxException {
        return request(HttpMethod.POST, uri, headers, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String get(String uri, Map<String, Object> params) throws IOException, URISyntaxException {
        return request(HttpMethod.GET, uri, null, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String get(String uri, Map<String, String> headers, Map<String, Object> params) throws IOException, URISyntaxException {
        return request(HttpMethod.GET, uri, headers, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String get(String uri, String params) throws IOException, URISyntaxException {
        return request(HttpMethod.GET, uri, null, params, ChronoUnit.MINUTE.getMillis());
    }

    public static String get(String uri, Map<String, String> headers, String params) throws IOException, URISyntaxException {
        return request(HttpMethod.GET, uri, headers, params, ChronoUnit.MINUTE.getMillis());
    }

    /**
     * 格式化接口参数
     */
    private static String formatParams(String contentType, Map<String, Object> params) {
        if (StringUtils.isNotBlank(contentType) && contentType.contains(ContentType.APPLICATION_JSON)) {
            return Jackson.toJsonString(params);
        }
        return HttpUtils.toFormData(params);
    }

    /**
     * url追加查询参数
     */
    private static String appendQueryParams(String url, String params) {
        if (StringUtils.isBlank(params)) {
            return url;
        }
        return url + (url.contains(Constants.QUESTION_MARK) ?
                Constants.AMPERSAND : Constants.QUESTION_MARK) + params;
    }

    /**
     * DELETE不支持body传参，GET也按习惯URL方式传参
     */
    private static boolean isQueryParams(HttpMethod method) {
       return HttpMethod.DELETE == method || HttpMethod.GET == method;
    }

    public static String request(
            HttpMethod method, String url, Map<String, String> headers, Map<String, Object> params, long timeout)
            throws IOException, URISyntaxException {
        return request(method, url, headers, formatParams(MapUtils.isEmpty(headers) ?
                null : headers.get(HttpHeaders.CONTENT_TYPE), params), timeout);
    }

    /**
     * params格式：key1=val1&key2=val2
     * @param timeout 超时时间，单位：毫秒
     */
    public static String request(
            HttpMethod method, String url, Map<String, String> headers, String params, long timeout)
            throws IOException, URISyntaxException {
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            boolean queryParams = isQueryParams(method);
            if (queryParams) {
                url = appendQueryParams(url, params);
            }
            connection = (HttpURLConnection) new URI(url).toURL().openConnection();
            if (TrustHttps.isHttps(url)) {
                TrustHttps.trustAllHosts((HttpsURLConnection) connection);
            }

            // 不使用缓存
            connection.setUseCaches(Boolean.FALSE);
            // 设置是否从connection读入
            connection.setDoInput(Boolean.TRUE);
            connection.setRequestMethod(method.name());
            /*
             * 是否向connection输出,参数放在http正文内时,要设为true。
             * HTTP method DELETE doesn't support output
             */
            connection.setDoOutput(!queryParams);
            connection.setConnectTimeout((int) timeout);
            connection.setReadTimeout((int) timeout);
            if (MapUtils.isNotEmpty(headers)) {
                /*
                 * 在Http Header中添加属性
                 */
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 配置必须要在connect之前完成
            connection.connect();
            if (!queryParams) {
                output = connection.getOutputStream();
                // 向输出流写数据
                output.write(params.getBytes());
                // 关闭流,不能再向输出流写入任何数据,先前写入的数据存在于内存缓冲区中,将流信息刷入缓冲输出流
                output.flush();
            }

            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                    connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED ||
                    connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                return IoUtils.getString(connection.getInputStream());
            } else{
                throw new IOException(IoUtils.getString(connection.getErrorStream()));
            }
        } finally {
            IoUtils.close(output);
            if (null != connection) {
                connection.disconnect();
            }
        }
    }
}
