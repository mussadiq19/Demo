package com.example.sovaibackend.domain.company.service;

import com.example.sovaibackend.domain.company.dto.CompanyRequest;
import com.example.sovaibackend.domain.company.dto.CompanyResponse;
import com.example.sovaibackend.domain.company.dto.DashboardResponse;

public interface CompanyService {
    CompanyResponse getById(Long id);
    CompanyResponse update(Long id, CompanyRequest request);
    DashboardResponse getDashboard(Long id);
}

