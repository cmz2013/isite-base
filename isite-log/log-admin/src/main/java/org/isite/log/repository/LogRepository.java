package org.isite.log.repository;

import org.isite.log.po.LogPo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LogRepository extends ElasticsearchRepository<LogPo, String> {

    List<LogPo> findByServiceId(String serviceId);
}