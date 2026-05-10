package com.example.sovaibackend.domain.company.dto;

public record AiReadinessResponse(
        int overallScore,
        int automationCoverage,
        int aiAdoption,
        int workforcePreparedness,
        String riskExposure
) {
}
