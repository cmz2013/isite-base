package org.isite.security.login;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.security.data.enums.CodeLoginMode;
import org.springframework.stereotype.Component;

/**
 * @Description 登录接口工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class CodeLoginFactory extends AbstractFactory<CodeLogin, CodeLoginMode, String> {

}
