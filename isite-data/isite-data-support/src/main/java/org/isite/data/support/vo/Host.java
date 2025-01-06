package org.isite.data.support.vo;

import lombok.Getter;

/**
 * @Description 主机信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class Host {
    /**
     * IP地址
     */
    private final String ip;
    /**
     * 端口
     */
    private final Integer port;

    public Host(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }
}
