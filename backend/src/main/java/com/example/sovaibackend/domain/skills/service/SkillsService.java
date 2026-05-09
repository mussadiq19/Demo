package com.sovai.platform.domain.skills.service;

import com.sovai.platform.domain.skills.dto.GapAnalysisResponse;
import com.sovai.platform.domain.skills.dto.SkillUploadRequest;

import java.util.List;

public interface SkillsService {
    int uploadSkills(SkillUploadRequest request);
    List<GapAnalysisResponse> companyGaps(Long companyId);
    GapAnalysisResponse employeeGap(Long userId);
}

