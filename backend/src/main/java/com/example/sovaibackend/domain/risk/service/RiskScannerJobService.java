package com.example.sovaibackend.domain.risk.service;

import com.example.sovaibackend.domain.risk.dto.RiskScanResult;

public interface RiskScannerJobService {
    RiskScanResult runScan(Long companyId);
}

