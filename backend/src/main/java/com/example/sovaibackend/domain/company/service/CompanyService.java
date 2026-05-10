package com.example.sovaibackend.domain.company.service;

import com.example.sovaibackend.domain.company.dto.CompanyRequest;
import com.example.sovaibackend.domain.company.dto.CompanyResponse;
import com.example.sovaibackend.domain.company.dto.DashboardResponse;
import com.example.sovaibackend.domain.company.dto.AiReadinessResponse;

public interface CompanyService {
    CompanyResponse getById(Long id);
    CompanyResponse update(Long id, CompanyRequest request);
    DashboardResponse getDashboard(Long id);
    AiReadinessResponse calculateAiReadiness(Long companyId);
}
