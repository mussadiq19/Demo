package com.sovai.platform.domain.company.controller;

import com.sovai.platform.common.response.ApiResponse;
import com.sovai.platform.domain.company.dto.CompanyRequest;
import com.sovai.platform.domain.company.dto.CompanyResponse;
import com.sovai.platform.domain.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ApiResponse<Map<String, Object>> dashboard(@PathVariable Long id) {
        return ApiResponse.ok(companyService.dashboard(id), "Dashboard reflects risk and workforce readiness");
    }
}

