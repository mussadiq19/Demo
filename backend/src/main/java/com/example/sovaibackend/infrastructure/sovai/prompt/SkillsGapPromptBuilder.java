package com.example.sovaibackend.infrastructure.sovai.prompt;

import com.example.sovaibackend.domain.auth.entity.User;
import com.example.sovaibackend.domain.skills.entity.EmployeeSkill;
import com.example.sovaibackend.infrastructure.ai.AiRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillsGapPromptBuilder {

    @Value("${sovai.api.model:claude-sonnet-4-6}")
    private String model;

    public AiRequest build(User employee, List<EmployeeSkill> currentSkills, List<String> industryDemands) {
        String system = """
                You are a workforce development advisor.
                Your mission is to keep employees skilled, employed, and growing.
                This is a tool for human empowerment — not replacement.
                You MUST return ONLY raw valid JSON.
                No markdown, no code fences, no explanation.
                Format:
                {
                  "gaps":[{"skillName":"","currentLevel":"","targetLevel":"","priority":"HIGH|MEDIUM|LOW"}],
                  "roadmap":[{"skillName":"","description":"","resourceUrl":"","estimatedHours":0,"stepOrder":0}]
                }
                """;

        String user = """
                Generate a skills gap analysis and upskilling roadmap for:
                Employee: %s
                Current Skills: %s
                Emerging Industry Demands: %s
                Return JSON only.
                """.formatted(
                    employee.getFullName(),
                    currentSkills.stream()
                        .map(s -> "skillId=" + s.getSkillId() + "(" + s.getProficiency() + ")")
                        .toList(),
                    industryDemands
                );

        return new AiRequest(model, system, user, 2000);
    }
}

