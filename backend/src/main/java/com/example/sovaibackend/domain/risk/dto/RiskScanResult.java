package com.example.sovaibackend.domain.risk.dto;

import java.util.List;

public record RiskScanResult(
    List<RiskItem> risks
) {
    public record RiskItem(
        String title,
        String description,
        String severity,
        String source,
        String mitigation
    ) {
    }
}

