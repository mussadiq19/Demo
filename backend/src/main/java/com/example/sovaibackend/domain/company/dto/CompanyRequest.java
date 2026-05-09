package com.sovai.platform.domain.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
    @NotBlank(message = "Company name is required")
    String name,

    String industry,
    String techStack,
    String supplyChainInfo
) {
}

