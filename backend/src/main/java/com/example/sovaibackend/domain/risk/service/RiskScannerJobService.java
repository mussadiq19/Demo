package com.sovai.platform.domain.risk.service;

import com.sovai.platform.domain.risk.dto.RiskScanResult;

public interface RiskScannerJobService {
    RiskScanResult runScan(Long companyId);
}

