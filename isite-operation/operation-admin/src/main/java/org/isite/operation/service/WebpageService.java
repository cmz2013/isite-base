package org.isite.operation.service;

import org.isite.commons.cloud.enums.TerminalType;
import org.isite.mybatis.service.PoService;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.mapper.WebpageMapper;
import org.isite.operation.po.WebpagePo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.isite.commons.lang.template.xml.Thymeleaf.process;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class WebpageService extends PoService<WebpagePo, Integer> {

    public WebpageService(WebpageMapper webpageMapper) {
        super(webpageMapper);
    }

    /**
     * 添加活动网页
     * @param activityId 活动ID
     * @param terminalType 用户终端类型
     * @param code 活动网页源码
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(Integer activityId, TerminalType terminalType, String code) {
        WebpagePo webpagePo = new WebpagePo();
        webpagePo.setActivityId(activityId);
        webpagePo.setCode(code);
        webpagePo.setTerminalType(terminalType);
        insert(webpagePo);
    }

    /**
     * 获取活动网页模板源码
     * @param activityId 活动ID
     * @param terminalType 用户终端类型
     * @return 源码
     */
    public WebpagePo getWebpage(int activityId, TerminalType terminalType) {
        WebpagePo webpagePo = new WebpagePo();
        webpagePo.setTerminalType(terminalType);
        webpagePo.setActivityId(activityId);
        return findOne(webpagePo);
    }

    /**
     * 获取页面Html代码
     */
    public String getWebpage(Activity activity, TerminalType terminalType) {
        WebpagePo webpagePo = getWebpage(activity.getId(), terminalType);
        return null != webpagePo ?
                process(webpagePo.getCode(), activity) : "webpage not found";
    }
}
