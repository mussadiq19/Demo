package com.sovai.platform.domain.skills.service;

import com.sovai.platform.common.audit.AuditListener;
import com.sovai.platform.domain.skills.dto.GapAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillsAnalysisJobServiceImpl implements SkillsAnalysisJobService, com.sovai.platform.domain.risk.service.AnalysisJob {

    private final SkillsService skillsService;
    private final AuditListener auditListener;

    @Override
    public List<GapAnalysisResponse> runCompanyAnalysis(Long companyId) {
        auditListener.logStart("SkillsAnalysisJob", companyId);
        List<GapAnalysisResponse> response = skillsService.companyGaps(companyId);
        auditListener.logEnd("SkillsAnalysisJob", companyId);
        return response;
    }

    @Override
    public void run(Long companyId) {
        runCompanyAnalysis(companyId);
    }
}

