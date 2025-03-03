package org.isite.data.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.data.support.vo.Host;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HostConverter {

    private HostConverter() {
    }

    public static List<Host> toHosts(List<ServiceInstance> servers) {
        return CollectionUtils.isEmpty(servers) ? Collections.emptyList() : servers.stream().map(server ->
                new Host(server.getHost(), server.getPort())).collect(Collectors.toList());
    }
}