package com.example.sovaibackend.domain.risk.dto;

import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RiskRequest(
    @NotNull
    Long companyId,

    Long categoryId,

    @NotBlank
    String title,

    String description,

    @NotNull
    RiskSeverity severity,

    String source
) {
}

