package com.example.sovaibackend.domain.skills.service;

import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;
import com.example.sovaibackend.domain.skills.dto.SkillUploadRequest;

import java.util.List;

public interface SkillsService {
    int uploadSkills(SkillUploadRequest request);
    List<GapAnalysisResponse> companyGaps(Long companyId);
    GapAnalysisResponse employeeGap(Long userId);
}

