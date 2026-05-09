package com.sovai.platform.domain.skills.service;

import com.sovai.platform.domain.skills.dto.GapAnalysisResponse;

import java.util.List;

public interface SkillsAnalysisJobService {
    List<GapAnalysisResponse> runCompanyAnalysis(Long companyId);
}

