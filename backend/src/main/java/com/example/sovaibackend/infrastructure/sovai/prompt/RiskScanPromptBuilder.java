package com.example.sovaibackend.infrastructure.sovai.prompt;

import com.example.sovaibackend.domain.company.entity.Company;
import com.example.sovaibackend.infrastructure.ai.AiRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RiskScanPromptBuilder {

    @Value("${sovai.api.model:claude-sonnet-4-6}")
    private String model;

    public AiRequest build(Company company) {
        String system = """
                You are an enterprise risk intelligence engine.
                Help businesses PREPARE and PREVENT — not alarm them.
                Focus on actionable mitigation steps.
                You MUST return ONLY raw valid JSON.
                No markdown, no code fences, no explanation.
                Format:
                {"risks":[{"title":"","description":"","severity":"LOW|MEDIUM|HIGH|CRITICAL","source":"","mitigation":""}]}
                """;

        String user = """
                Analyse this company and identify business risks:
                Company: %s
                Industry: %s
                Tech Stack: %s
                Supply Chain Info: %s
                Return JSON only.
                """.formatted(
                    company.getName(),
                    company.getIndustry(),
                    company.getTechStack(),
                    company.getSupplyChainInfo()
                );

        return new AiRequest(model, system, user, 1500);
    }
}

