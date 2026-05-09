package com.example.sovaibackend.domain.skills.dto;

import java.util.List;

public record GapAnalysisResponse(
    Long userId,
    List<String> missingSkills,
    String priority,
    String recommendation
) {
}

