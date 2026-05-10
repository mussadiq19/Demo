package com.example.sovaibackend.domain.company.dto;

public record CompanyResponse(
    Long id,
    String name,
    String industry,
    String size,
    String techStack,
    String supplyChainInfo
) {
}
