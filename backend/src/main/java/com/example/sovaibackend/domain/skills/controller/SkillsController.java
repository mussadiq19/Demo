package com.example.sovaibackend.domain.skills.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;
import com.example.sovaibackend.domain.skills.dto.SkillUploadRequest;
import com.example.sovaibackend.domain.skills.service.SkillsAnalysisJobService;
import com.example.sovaibackend.domain.skills.service.SkillsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillsController {

    private final SkillsService skillsService;
    private final SkillsAnalysisJobService skillsAnalysisJobService;

    @PostMapping("/upload")
    public ApiResponse<Integer> upload(@Valid @RequestBody SkillUploadRequest request) {
        return ApiResponse.ok(skillsService.uploadSkills(request), "Skills uploaded to strengthen workforce readiness");
    }

    @GetMapping("/gaps")
    public ApiResponse<List<GapAnalysisResponse>> companyGaps(@RequestParam Long companyId) {
        return ApiResponse.ok(skillsAnalysisJobService.runCompanyAnalysis(companyId));
    }

    @GetMapping("/gaps/employee/{userId}")
    public ApiResponse<GapAnalysisResponse> employeeGap(@PathVariable Long userId) {
        return ApiResponse.ok(skillsService.employeeGap(userId));
    }
}

