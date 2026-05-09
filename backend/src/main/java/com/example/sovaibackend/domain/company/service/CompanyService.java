package com.example.sovaibackend.domain.company.service;

import com.example.sovaibackend.domain.company.dto.CompanyRequest;
import com.example.sovaibackend.domain.company.dto.CompanyResponse;

import java.util.Map;

public interface CompanyService {
    CompanyResponse getById(Long id);
    CompanyResponse update(Long id, CompanyRequest request);
    Map<String, Object> dashboard(Long id);
}

