package org.isite.operation.converter;

import org.isite.operation.po.WebpagePo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class WebpageConverter {

    private WebpageConverter() {
    }

    public static WebpagePo toWebpagePo(int activityId) {
        WebpagePo webpagePo = new WebpagePo();
        webpagePo.setActivityId(activityId);
        return webpagePo;
    }
}
