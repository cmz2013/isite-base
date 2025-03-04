package org.isite.security.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.security.data.constants.SecurityUrls;
import org.isite.security.data.dto.CaptchaDto;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
/**
 * @Description 通过FeignClientFactory获取客户端实例，头部传递token、version等信息
 * @Author <font color='blue'>zhangcm</font>
 */
public interface CaptchaClient {
    /**
     * @Description 校验验证码
     * feign的get方式默认不能解析对象，只支持基本类型参数，对象类型自动转POST请求。
     * 使用openfeign提供的@SpringQueryMap可以将对象属性转为url中的查询参数
     */
    @DeleteMapping(SecurityUrls.DELETE_CAPTCHA)
    Result<Boolean> checkCaptcha(@SpringQueryMap CaptchaDto captchaDto);
}
