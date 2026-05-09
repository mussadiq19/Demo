package com.sovai.platform.domain.company.service;

import com.sovai.platform.domain.company.dto.CompanyRequest;
import com.sovai.platform.domain.company.dto.CompanyResponse;

import java.util.Map;

public interface CompanyService {
    CompanyResponse getById(Long id);
    CompanyResponse update(Long id, CompanyRequest request);
    Map<String, Object> dashboard(Long id);
}

