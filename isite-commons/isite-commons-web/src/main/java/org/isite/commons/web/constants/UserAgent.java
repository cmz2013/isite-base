package org.isite.commons.web.constants;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.cloud.data.enums.TerminalType;
import org.isite.commons.web.utils.RequestUtils;

/**
 * @Description 用户终端 agent
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserAgent {

    private UserAgent() {
    }

    /**
     * web端
     */
    public static final String MSIE = "msie";
    public static final String TRIDENT = "trident";
    public static final String EDGE = "edge";
    public static final String OPERA = "opera";

    /**
     * app端
     */
    public static final String IPAD = "ipad";
    public static final String ANDROID = "android";
    public static final String IPHONE = "iphone";
    public static final String TABLET = "tablet";

    /**
     * 获取用户终端类型
     */
    public static TerminalType getTerminalType() {
        String agent = RequestUtils.getRequest().getHeader(HttpHeaders.USER_AGENT);
        if (StringUtils.isNotBlank(agent)) {
            agent = agent.toLowerCase();
            if(agent.contains(ANDROID) || agent.contains(IPAD) ||
                    agent.contains(IPHONE) || agent.contains(TABLET)) {
                return TerminalType.APP;
            }
        }
        return TerminalType.WEB;
    }
}