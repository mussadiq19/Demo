package com.sovai.platform.common.audit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditListener {

    public void logStart(String jobName, Long companyId) {
        log.info("Job {} started for companyId={}", jobName, companyId);
    }

    public void logEnd(String jobName, Long companyId) {
        log.info("Job {} ended for companyId={}", jobName, companyId);
    }
}

