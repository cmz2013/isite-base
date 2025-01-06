package org.isite.commons.lang.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class IoUtils {

    private IoUtils() {
    }

    /**
     * 读取字符串
     */
    public static String getString(InputStream input) throws IOException {
        return new String(getBytes(input));
    }

    /**
     * 读取字符串
     */
    public static String getString(InputStream input, Charset charset) throws IOException {
        return new String(getBytes(input), charset);
    }

    /**
     * 读取字节数组
     */
    public static byte[] getBytes(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(input, output);
            return output.toByteArray();
        }
    }

    /**
     * 复制流，注意：dest流使用完不关闭，可以继续写入
     */
    public static void copy(InputStream src, OutputStream dest) throws IOException {
        try (src) {
            int n;
            byte[] buffer = new byte[1024];
            while (-1 != (n = src.read(buffer))) {
                dest.write(buffer, ZERO, n);
            }
        }
    }

    /**
     * 读取字符串集合
     */
    public static List<String> getStringList(InputStream input) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(input);
             BufferedReader br = new BufferedReader(isr, 1024)) {
            return br.lines().collect(toList());
        }
    }

    /**
     * 读取字符串集合
     */
    public static List<String> getStringList(InputStream input, Charset charset) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(input, charset);
            BufferedReader br = new BufferedReader(isr, 1024)) {
            return br.lines().collect(toList());
        }
    }

    /**
     * 释放资源
     */
    public static void close(Closeable... closeables) {
        if (isEmpty(closeables)) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                if (null != closeable) {
                    closeable.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
