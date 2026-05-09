package com.example.sovaibackend.domain.risk.dto;

import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record RiskResponse(
    Long id,
    Long companyId,
    Long categoryId,
    String title,
    String description,
    RiskSeverity severity,
    RiskStatus status,
    BigDecimal aiConfidence,
    Instant detectedAt,
    Instant resolvedAt,
    String source
) {
}

