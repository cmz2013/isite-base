package org.isite.log.service;

import org.isite.log.po.LogPo;
import org.isite.log.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private LogRepository logRepository;

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public LogPo save(LogPo logPo) {
        return logRepository.save(logPo);
    }

    public List<LogPo> findByServiceId(String serviceId) {
        return logRepository.findByServiceId(serviceId);
    }

    public Iterable<LogPo> findAll() {
        return logRepository.findAll();
    }
}