package com.sovai.platform.domain.risk.scheduler;

import com.sovai.platform.domain.risk.service.RiskScannerJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RiskScannerJob implements Job {

    private final RiskScannerJobService riskScannerJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long companyId = context.getMergedJobDataMap().containsKey("companyId")
                ? context.getMergedJobDataMap().getLong("companyId")
                : 1L;
        log.info("Executing risk scan quartz job for companyId={}", companyId);
        riskScannerJobService.runScan(companyId);
    }
}

