package org.isite.data.converter;

import org.isite.data.support.vo.Host;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class HostConverter {

    private HostConverter() {
    }

    public static List<Host> toHosts(List<ServiceInstance> servers) {
        return isEmpty(servers) ? emptyList() : servers.stream().map(server ->
                new Host(server.getHost(), server.getPort())).collect(toList());
    }
}