package org.isite.data.client;

import org.isite.commons.cloud.factory.AbstractFactory;
import org.isite.data.support.enums.WsProtocol;
import org.springframework.stereotype.Component;

/**
 * @Description 工厂类
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WsClientFactory extends AbstractFactory<WsClient, WsProtocol, String> {
}
