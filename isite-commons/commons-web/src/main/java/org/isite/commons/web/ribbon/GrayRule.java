package org.isite.commons.web.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.isite.commons.cloud.constants.CloudConstants.X_VERSION;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getVersion;

/**
 * @Description 基于服务版本号的灰度发布：将部分流量引导到新版本的服务，同时保持大部分流量在旧版本上，从而进行逐步验证和测试
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class GrayRule extends ZoneAvoidanceRule {

    /**
     * @Description 重写ribbon断言，它的作用就是在众多Server的列表中，通过一定的过滤策略，T除不合格的Server，留下来合格的Server列表，进而供以选择。
     */
    @Override
    public AbstractServerPredicate getPredicate() {
        return new AbstractServerPredicate() {
            //断言方法
            @Override
            public boolean apply(PredicateKey predicateKey) {
                String targetVersion = getVersion();
                if (isBlank(targetVersion)) {
                    return true;
                }
                NacosServer server = cast(predicateKey.getServer());
                final Map<String, String> metadata = server.getInstance().getMetadata();
                if (isBlank(metadata.get(X_VERSION))) {
                    return true;
                }
                return metadata.get(X_VERSION).equals(targetVersion);
            }
        };
    }
}
