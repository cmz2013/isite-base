package org.isite.security.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.security.data.dto.VerificationCodeDto;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;

import static org.isite.security.data.constants.UrlConstants.DELETE_VERIFICATION_CODE;

/**
 * @Description 通过FeignClientFactory获取客户端实例，头部传递token、version等信息
 * @Author <font color='blue'>zhangcm</font>
 */
public interface VerificationCodeClient {
    /**
     * @Description 校验验证码
     * feign的get方式默认不能解析对象，只支持基本类型参数，对象类型自动转POST请求。
     * 使用openfeign提供的@SpringQueryMap可以将对象属性转为url中的查询参数
     */
    @DeleteMapping(DELETE_VERIFICATION_CODE)
    Result<Boolean> checkCode(@SpringQueryMap VerificationCodeDto codeDto);
}
