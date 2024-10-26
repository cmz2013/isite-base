package org.isite.security.code;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.security.data.enums.VerificationCodeType;
import org.springframework.stereotype.Component;

/**
 * @Description 验证码工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class CodeExecutorFactory extends AbstractFactory<CodeExecutor, VerificationCodeType, String> {
}
