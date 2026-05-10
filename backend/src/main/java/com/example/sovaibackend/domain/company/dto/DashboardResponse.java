package com.example.sovaibackend.domain.company.dto;

public record DashboardResponse(
    int criticalRiskCount,
    int highRiskCount,
    String criticalRiskTrend,
    String highRiskTrend,
    int skillsGapPercentage,
    String skillsGapTrend,
    int roadmapProgress,
    String roadmapTrend,
    String lastScanAt,
    int signalsProcessed,
    String latestInsight
) {}
