package com.sovai.platform.domain.risk.service;

import com.sovai.platform.common.response.PagedResponse;
import com.sovai.platform.domain.risk.dto.RiskRequest;
import com.sovai.platform.domain.risk.dto.RiskResponse;
import com.sovai.platform.domain.risk.entity.RiskSeverity;
import com.sovai.platform.domain.risk.entity.RiskStatus;

import java.util.Map;

public interface RiskService {
    PagedResponse<RiskResponse> list(Long companyId, RiskStatus status, RiskSeverity severity, int page, int size);
    RiskResponse getById(Long id);
    RiskResponse create(RiskRequest request);
    RiskResponse acknowledge(Long id);
    RiskResponse resolve(Long id);
    Map<String, Object> stats(Long companyId);
}

