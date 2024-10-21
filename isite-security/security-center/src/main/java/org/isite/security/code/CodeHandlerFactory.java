package org.isite.security.code;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.security.data.enums.CodeMode;
import org.springframework.stereotype.Component;

/**
 * @Description 验证码工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class CodeHandlerFactory extends AbstractFactory<CodeHandler, CodeMode, String> {
}
