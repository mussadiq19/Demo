package com.example.sovaibackend.domain.skills.service;

import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;

import java.util.List;

public interface SkillsAnalysisJobService {
    List<GapAnalysisResponse> runCompanyAnalysis(Long companyId);
}

