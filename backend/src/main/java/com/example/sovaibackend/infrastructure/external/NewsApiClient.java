package com.example.sovaibackend.infrastructure.external;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsApiClient {
    public List<String> fetchIndustrySignals(String industry) {
        return List.of("Market volatility remains moderate", "Regulatory shifts expected in next quarter");
    }
}

