package com.example.sovaibackend.domain.company.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.common.security.UserPrincipal;
import com.example.sovaibackend.domain.company.dto.AiReadinessResponse;
import com.example.sovaibackend.domain.company.dto.CompanyRequest;
import com.example.sovaibackend.domain.company.dto.CompanyResponse;
import com.example.sovaibackend.domain.company.dto.DashboardResponse;
import com.example.sovaibackend.domain.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(companyService.getById(id), "Company profile loaded");
    }

    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> update(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return ApiResponse.ok(companyService.update(id, request), "Company profile updated for better preparedness");
    }

    @GetMapping("/{id}/dashboard")
    public ApiResponse<DashboardResponse> dashboard(@PathVariable Long id) {
        return ApiResponse.ok(companyService.getDashboard(id), "Dashboard reflects risk and workforce readiness");
    }

    @GetMapping("/{id}/ai-readiness")
    public ResponseEntity<ApiResponse<AiReadinessResponse>> getAiReadiness(@PathVariable Long id,
                                                                           @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.calculateAiReadiness(id)));
    }
}
