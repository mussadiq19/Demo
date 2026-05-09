package com.sovai.platform.domain.company.dto;

public record CompanyResponse(
    Long id,
    String name,
    String industry,
    String techStack,
    String supplyChainInfo
) {
}

