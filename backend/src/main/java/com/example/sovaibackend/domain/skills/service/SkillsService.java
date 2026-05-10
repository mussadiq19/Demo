package com.example.sovaibackend.domain.skills.service;

import com.example.sovaibackend.domain.skills.dto.CompanyGapResponse;
import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;
import com.example.sovaibackend.domain.skills.dto.SkillUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SkillsService {
    int uploadSkills(SkillUploadRequest request);
    int processUpload(MultipartFile file, Long userId);
    List<GapAnalysisResponse> companyGaps(Long companyId);
    CompanyGapResponse getGaps(Long companyId);
    GapAnalysisResponse employeeGap(Long userId);
}
