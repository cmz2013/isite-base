package org.isite.commons.web.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.lang.utils.IoUtils;
import org.isite.commons.web.constants.UserAgent;
import org.isite.commons.web.utils.RequestUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FileResponse {

    private FileResponse() {
    }

    /**
     * @Description response写入文件信息
     */
    public static void write(String filename, byte[] bytes) throws IOException {
        HttpServletResponse response = RequestUtils.getResponse();
        resetResponse(filename, RequestUtils.getRequest(), response);
        try (OutputStream ous = response.getOutputStream()) {
            ous.write(bytes);
        }
    }

    /**
     * @Description response写入文件信息
     */
    public static void write(String filename, Workbook workbook) throws IOException {
        HttpServletResponse response = RequestUtils.getResponse();
        resetResponse(filename, RequestUtils.getRequest(), response);
        try (OutputStream ous = response.getOutputStream()) {
            workbook.write(ous);
        } finally {
            IoUtils.close(workbook);
        }
    }

    private static void resetResponse(
            String filename, HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        String agent = request.getHeader(HttpHeaders.USER_AGENT);
        String attachment = "filename=\"" + new String(
                filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"";
        if (StringUtils.isNotBlank(agent)) {
            agent = agent.toLowerCase();
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            if (agent.contains(UserAgent.MSIE) || agent.contains(UserAgent.TRIDENT) || agent.contains(UserAgent.EDGE)) {
                attachment = "filename=\"" + filename + "\"";
            } else if (agent.contains(UserAgent.OPERA)) {
                attachment = "filename*=UTF-8''" + filename;
            }
        }
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;" + attachment);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
