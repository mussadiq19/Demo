package com.example.sovaibackend.domain.skills.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.common.security.UserPrincipal;
import com.example.sovaibackend.domain.skills.dto.CompanyGapResponse;
import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;
import com.example.sovaibackend.domain.skills.dto.SkillUploadRequest;
import com.example.sovaibackend.domain.skills.service.SkillsAnalysisJobService;
import com.example.sovaibackend.domain.skills.service.SkillsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Integer> uploadFile(@RequestParam("file") MultipartFile file,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }
        return ApiResponse.ok(
                skillsService.processUpload(file, principal.getId()),
                "Skills file uploaded. AI analysis will begin automatically."
        );
    }

    @GetMapping("/gaps")
    public ApiResponse<CompanyGapResponse> companyGaps(@RequestParam Long companyId) {
        return ApiResponse.ok(skillsService.getGaps(companyId));
    }

    @GetMapping("/gaps/employee/{userId}")
    public ApiResponse<GapAnalysisResponse> employeeGap(@PathVariable Long userId) {
        return ApiResponse.ok(skillsService.employeeGap(userId));
    }
}
