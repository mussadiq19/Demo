package com.sovai.platform.domain.skills.scheduler;

import com.sovai.platform.domain.skills.service.SkillsAnalysisJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SkillsAnalysisJob implements Job {

    private final SkillsAnalysisJobService skillsAnalysisJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long companyId = context.getMergedJobDataMap().containsKey("companyId")
                ? context.getMergedJobDataMap().getLong("companyId")
                : 1L;
        log.info("Executing skills analysis quartz job for companyId={}", companyId);
        skillsAnalysisJobService.runCompanyAnalysis(companyId);
    }
}

