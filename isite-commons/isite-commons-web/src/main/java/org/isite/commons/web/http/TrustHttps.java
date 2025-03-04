package org.isite.commons.web.http;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.lang.Constants;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
/**
 * @Description HTTPS SSL认证
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class TrustHttps {

    private TrustHttps() {
    }

    private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] {new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    }};

    public static void trustAllHosts(HttpsURLConnection connection) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, TRUST_ALL_CERTS, new SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        connection.setHostnameVerifier((hostname, session) -> Boolean.TRUE);
    }

    public static boolean isHttps(String url) {
        return "https".equalsIgnoreCase(url.substring(Constants.ZERO, Constants.FIVE));
    }
}
