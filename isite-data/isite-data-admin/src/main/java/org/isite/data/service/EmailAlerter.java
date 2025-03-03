package org.isite.data.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.template.FreeMarker;
import org.isite.commons.web.email.EmailClient;
import org.isite.commons.web.email.EmailConfig;
import org.isite.data.po.DataApiPo;
import org.isite.data.po.DataLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description email邮件提醒错误日志
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
@ConditionalOnBean(value = EmailConfig.class)
public class EmailAlerter {

    private EmailClient emailClient;
    private DataApiService dataApiService;

    /**
     * @param logPo 接口日志
     */
    public void alert(DataLogPo logPo) {
        DataApiPo dataApi = dataApiService.get(logPo.getApiId());
        /*
         * StringUtils.split()是把分隔符拆成一个个单独的字符，再用这些字符去把字符串进行分割的，
         * 只要匹配到了分隔符中的任意一个字符，就会进行分割。
         * 而String.split()是把分隔符作为一个整体来对字符串进行分割。
         */
        if (StringUtils.isBlank(dataApi.getEmails())) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("dataApi", dataApi);
        data.put("dataLog", logPo);

        for (String to : dataApi.getEmails().split(Constants.COMMA)) {
            try {
                emailClient.sendEmail(to, emailClient.getFromName() + Constants.SPACE +
                                MessageUtils.getMessage("EmailAlerter.subject", "Alarm"),
                        FreeMarker.process(UrlConstants.URL_TEMPLATES, "alert_email.ftl", data));
            } catch (Exception e) {
                log.error("告警邮件发送失败", e);
            }
        }
    }

    @Autowired
    public void setEmailClient(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    @Autowired
    public void setDataApiService(DataApiService dataApiService) {
        this.dataApiService = dataApiService;
    }
}
