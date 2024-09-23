package org.isite.commons.web.file;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.utils.IoUtils.close;
import static org.isite.commons.cloud.constants.HttpHeaders.CONTENT_DISPOSITION;
import static org.isite.commons.cloud.constants.HttpHeaders.USER_AGENT;
import static org.isite.commons.web.utils.RequestUtils.getRequest;
import static org.isite.commons.web.utils.RequestUtils.getResponse;
import static org.isite.commons.web.constants.UserAgent.EDGE;
import static org.isite.commons.web.constants.UserAgent.MSIE;
import static org.isite.commons.web.constants.UserAgent.OPERA;
import static org.isite.commons.web.constants.UserAgent.TRIDENT;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

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
        HttpServletResponse response = getResponse();
        resetResponse(filename, getRequest(), response);
        try (OutputStream ous = response.getOutputStream()) {
            ous.write(bytes);
        }
    }

    /**
     * @Description response写入文件信息
     */
    public static void write(String filename, Workbook workbook) throws IOException {
        HttpServletResponse response = getResponse();
        resetResponse(filename, getRequest(), response);
        try (OutputStream ous = response.getOutputStream()) {
            workbook.write(ous);
        } finally {
            close(workbook);
        }
    }

    private static void resetResponse(
            String filename, HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        String agent = request.getHeader(USER_AGENT);
        String attachment = "filename=\"" + new String(filename.getBytes(UTF_8), ISO_8859_1) + "\"";

        if (isNotBlank(agent)) {
            agent = agent.toLowerCase();
            filename = encode(filename, UTF_8);
            if (agent.contains(MSIE) || agent.contains(TRIDENT) || agent.contains(EDGE)) {
                attachment = "filename=\"" + filename + "\"";
            } else if (agent.contains(OPERA)) {
                attachment = "filename*=UTF-8''" + filename;
            }
        }
        response.addHeader(CONTENT_DISPOSITION, "attachment;" + attachment);
        response.setContentType(APPLICATION_OCTET_STREAM.toString());
    }
}
