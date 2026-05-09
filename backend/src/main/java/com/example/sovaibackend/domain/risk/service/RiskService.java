package com.example.sovaibackend.domain.risk.service;

import com.example.sovaibackend.common.response.PagedResponse;
import com.example.sovaibackend.domain.risk.dto.RiskRequest;
import com.example.sovaibackend.domain.risk.dto.RiskResponse;
import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;

import java.util.Map;

public interface RiskService {
    PagedResponse<RiskResponse> list(Long companyId, RiskStatus status, RiskSeverity severity, int page, int size);
    RiskResponse getById(Long id);
    RiskResponse create(RiskRequest request);
    RiskResponse acknowledge(Long id);
    RiskResponse resolve(Long id);
    Map<String, Object> stats(Long companyId);
}

